package com.androidforge.habitflow.data.repository

import com.androidforge.habitflow.data.local.dao.HabitCompletionDao
import com.androidforge.habitflow.data.local.dao.HabitDao
import com.androidforge.habitflow.data.mapper.toDomain
import com.androidforge.habitflow.data.mapper.toEntity
import com.androidforge.habitflow.domain.model.CompletionStatus
import com.androidforge.habitflow.domain.model.Habit
import com.androidforge.habitflow.domain.model.HabitCompletion
import com.androidforge.habitflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [HabitRepository] using Room DAOs.
 * This class handles the mapping between domain models and Room entities.
 */
@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitCompletionDao: HabitCompletionDao
) : HabitRepository {

    // --- Habit Operations ---

    override suspend fun addHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habitId: String) {
        // First delete completions, then the habit itself to maintain referential integrity
        // (though Room's CASCADE should handle this, explicit deletion can be safer if logic changes)
        habitCompletionDao.deleteCompletionsForHabit(habitId)
        habitDao.setHabitActiveStatus(habitId, false) // Soft delete by setting isActive to false
    }

    override fun getHabitById(habitId: String): Flow<Habit?> {
        return habitDao.getHabitById(habitId).map { it?.toDomain() }
    }

    override fun getAllActiveHabits(): Flow<List<Habit>> {
        return habitDao.getAllActiveHabits().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // --- Habit Completion Operations ---

    override suspend fun upsertHabitCompletion(
        habitId: String,
        date: LocalDate,
        status: CompletionStatus
    ) {
        val existingCompletion = habitCompletionDao.getHabitCompletionForDate(habitId, date).map { it?.toDomain() }.firstOrNull()
        val completion = existingCompletion?.copy(status = status) ?: HabitCompletion(habitId = habitId, date = date, status = status)
        habitCompletionDao.upsertHabitCompletion(completion.toEntity())
    }

    override fun getHabitCompletionForDate(habitId: String, date: LocalDate): Flow<HabitCompletion?> {
        return habitCompletionDao.getHabitCompletionForDate(habitId, date).map { it?.toDomain() }
    }

    override fun getHabitCompletionHistory(habitId: String): Flow<List<HabitCompletion>> {
        return habitCompletionDao.getHabitCompletionHistory(habitId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCompletionsByDate(date: LocalDate): Flow<List<HabitCompletion>> {
        return habitCompletionDao.getCompletionsByDate(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}