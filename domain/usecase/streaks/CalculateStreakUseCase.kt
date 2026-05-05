package com.androidforge.habitflow.domain.usecase.streaks

import com.androidforge.habitflow.core.util.DateUtils
import com.androidforge.habitflow.domain.model.CompletionStatus
import com.androidforge.habitflow.domain.model.Habit
import com.androidforge.habitflow.domain.model.HabitCompletion
import com.androidforge.habitflow.domain.model.HabitFrequency
import com.androidforge.habitflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject

/**
 * Represents the streak information for a habit.
 */
data class StreakInfo(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastCompletionDate: LocalDate? = null,
    val isCurrentlyActive: Boolean = false // True if the habit was completed today/its last scheduled day
)

/**
 * Use case for calculating current and longest streaks for a habit based on its completion history.
 */
class CalculateStreakUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: String, today: LocalDate = LocalDate.now()): StreakInfo {
        val habit = repository.getHabitById(habitId).firstOrNull()
            ?: return StreakInfo(isCurrentlyActive = false)
        val history = repository.getHabitCompletionHistory(habitId).firstOrNull().orEmpty()

        if (history.isEmpty()) return StreakInfo(isCurrentlyActive = false)

        return when (habit.frequency) {
            HabitFrequency.Daily -> calculateDailyStreak(habit, history, today)
            is HabitFrequency.SpecificDays -> calculateSpecificDaysStreak(habit, history, today)
            is HabitFrequency.Weekly -> calculateWeeklyStreak(habit, history, today)
        }
    }

    private fun calculateDailyStreak(habit: Habit, history: List<HabitCompletion>, today: LocalDate): StreakInfo {
        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0
        var lastCompletionDate: LocalDate? = null
        var isCurrentlyActive = false

        val completionMap = history.associateBy { it.date }

        var currentDate = today
        var foundCompletionTodayOrYesterday = false

        // Calculate current streak
        while (currentDate >= habit.createdAt) {
            val completion = completionMap[currentDate]
            if (completion?.status == CompletionStatus.COMPLETED) {
                tempStreak++
                if (lastCompletionDate == null) lastCompletionDate = currentDate
                if (currentDate == today) isCurrentlyActive = true
                foundCompletionTodayOrYesterday = true
            } else if (completion?.status == CompletionStatus.SKIPPED) {
                // Skipped days don't break the streak, but don't count towards it.
                // We need to check the day *before* the skipped day to see if the streak continues.
                // This logic is tricky, for now, treat skipped as non-completed.
                // A more robust skipped logic might require looking at previous 'valid' completion.
                if (tempStreak > 0) break // Streak breaks if a non-completed day is encountered after a completion
            } else { // MISSED or no entry
                // If it's today and not completed, it's not active
                if (currentDate == today) isCurrentlyActive = false
                if (tempStreak > 0) break // Streak breaks
            }
            currentDate = currentDate.minusDays(1)
        }
        currentStreak = tempStreak
        longestStreak = maxOf(longestStreak, currentStreak)

        // Recalculate longest streak by iterating through all history
        tempStreak = 0
        var currentLongestStreakDate = habit.createdAt
        while (currentLongestStreakDate <= today) {
            val completion = completionMap[currentLongestStreakDate]
            if (completion?.status == CompletionStatus.COMPLETED) {
                tempStreak++
            } else if (completion?.status == CompletionStatus.SKIPPED) {
                // Skipped logic for longest streak can be more lenient, depending on definition
                // For now, treat as non-completed for streak counting.
                longestStreak = maxOf(longestStreak, tempStreak)
                tempStreak = 0
            } else { // MISSED or no entry
                longestStreak = maxOf(longestStreak, tempStreak)
                tempStreak = 0
            }
            currentLongestStreakDate = currentLongestStreakDate.plusDays(1)
        }
        longestStreak = maxOf(longestStreak, tempStreak) // Final check for streak ending at 'today'

        // Refine isCurrentlyActive: if current streak is > 0, and the current day (or relevant day) is completed.
        // If lastCompletionDate is not null and is today, then it's active.
        // If lastCompletionDate is yesterday, it's still active for a daily habit.
        // If there's no completion today, but current streak is 0, it's not active.
        val latestCompletion = history.filter { it.status == CompletionStatus.COMPLETED }.maxByOrNull { it.date }
        isCurrentlyActive = latestCompletion?.date == today || latestCompletion?.date == today.minusDays(1) && currentStreak > 0

        return StreakInfo(currentStreak, longestStreak, latestCompletion?.date, isCurrentlyActive)
    }

    private fun calculateSpecificDaysStreak(habit: Habit, history: List<HabitCompletion>, today: LocalDate): StreakInfo {
        val specificDays = (habit.frequency as HabitFrequency.SpecificDays).daysOfWeek
        if (specificDays.isEmpty()) return StreakInfo(isCurrentlyActive = false)

        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0
        var lastCompletionDate: LocalDate? = null
        var isCurrentlyActive = false

        val completionMap = history.associateBy { it.date }

        // Current streak calculation
        var currentDate = today
        var daysChecked = 0
        while (currentDate >= habit.createdAt && daysChecked < 365) { // Limit days checked to prevent infinite loop
            if (currentDate.dayOfWeek in specificDays) {
                daysChecked++
                val completion = completionMap[currentDate]
                if (completion?.status == CompletionStatus.COMPLETED) {
                    tempStreak++
                    if (lastCompletionDate == null) lastCompletionDate = currentDate
                    if (currentDate == today) isCurrentlyActive = true
                } else if (completion?.status == CompletionStatus.SKIPPED) {
                    // Skipped on a specific day breaks streak for specific days frequency
                    if (tempStreak > 0) break
                } else { // MISSED or no entry
                    if (currentDate == today) isCurrentlyActive = false
                    if (tempStreak > 0) break
                }
            }
            currentDate = currentDate.minusDays(1)
        }
        currentStreak = tempStreak
        longestStreak = maxOf(longestStreak, currentStreak)

        // Longest streak calculation (iterate forward from habit creation)
        tempStreak = 0
        currentDate = habit.createdAt
        while (currentDate <= today) {
            if (currentDate.dayOfWeek in specificDays) {
                val completion = completionMap[currentDate]
                if (completion?.status == CompletionStatus.COMPLETED) {
                    tempStreak++
                } else if (completion?.status == CompletionStatus.SKIPPED) {
                    longestStreak = maxOf(longestStreak, tempStreak)
                    tempStreak = 0
                } else { // MISSED or no entry
                    longestStreak = maxOf(longestStreak, tempStreak)
                    tempStreak = 0
                }
            }
            currentDate = currentDate.plusDays(1)
        }
        longestStreak = maxOf(longestStreak, tempStreak) // Final check

        val latestCompletion = history.filter { it.status == CompletionStatus.COMPLETED }.maxByOrNull { it.date }
        isCurrentlyActive = latestCompletion?.date == today && today.dayOfWeek in specificDays && currentStreak > 0

        return StreakInfo(currentStreak, longestStreak, latestCompletion?.date, isCurrentlyActive)
    }

    private fun calculateWeeklyStreak(habit: Habit, history: List<HabitCompletion>, today: LocalDate): StreakInfo {
        val targetTimesPerWeek = (habit.frequency as HabitFrequency.Weekly).timesPerWeek
        if (targetTimesPerWeek <= 0) return StreakInfo(isCurrentlyActive = false)

        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0
        var lastCompletionDate: LocalDate? = null
        var isCurrentlyActive = false

        val groupedCompletionsByWeek = history.groupBy { DateUtils.getStartOfWeek(it.date) }
            .mapValues { (_, completions) -> completions.filter { it.status == CompletionStatus.COMPLETED }.size }

        // Current streak calculation
        var currentWeekStart = DateUtils.getStartOfWeek(today)
        while (currentWeekStart >= DateUtils.getStartOfWeek(habit.createdAt)) {
            val completionsInWeek = groupedCompletionsByWeek[currentWeekStart] ?: 0
            if (completionsInWeek >= targetTimesPerWeek) {
                tempStreak++
                // Find the latest completion date within this week that contributed to meeting the target
                lastCompletionDate = history.filter { it.date >= currentWeekStart && it.date < currentWeekStart.plusWeeks(1) && it.status == CompletionStatus.COMPLETED }.maxByOrNull { it.date } ?: lastCompletionDate
                if (currentWeekStart == DateUtils.getStartOfWeek(today)) isCurrentlyActive = true
            } else {
                if (tempStreak > 0) break
            }
            currentWeekStart = currentWeekStart.minusWeeks(1)
        }
        currentStreak = tempStreak
        longestStreak = maxOf(longestStreak, currentStreak)

        // Longest streak calculation (iterate forward)
        tempStreak = 0
        currentWeekStart = DateUtils.getStartOfWeek(habit.createdAt)
        while (currentWeekStart <= DateUtils.getStartOfWeek(today)) {
            val completionsInWeek = groupedCompletionsByWeek[currentWeekStart] ?: 0
            if (completionsInWeek >= targetTimesPerWeek) {
                tempStreak++
            } else {
                longestStreak = maxOf(longestStreak, tempStreak)
                tempStreak = 0
            }
            currentWeekStart = currentWeekStart.plusWeeks(1)
        }
        longestStreak = maxOf(longestStreak, tempStreak)

        val latestCompletion = history.filter { it.status == CompletionStatus.COMPLETED }.maxByOrNull { it.date }
        isCurrentlyActive = (groupedCompletionsByWeek[DateUtils.getStartOfWeek(today)] ?: 0) >= targetTimesPerWeek

        return StreakInfo(currentStreak, longestStreak, latestCompletion?.date, isCurrentlyActive)
    }
}