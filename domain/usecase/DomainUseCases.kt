package com.androidforge.habitflow.domain.usecase

import com.androidforge.habitflow.domain.usecase.completion.GetHabitCompletionHistoryUseCase
import com.androidforge.habitflow.domain.usecase.completion.GetHabitCompletionsForDateUseCase
import com.androidforge.habitflow.domain.usecase.completion.TrackHabitCompletionUseCase
import com.androidforge.habitflow.domain.usecase.habits.AddHabitUseCase
import com.androidforge.habitflow.domain.usecase.habits.DeleteHabitUseCase
import com.androidforge.habitflow.domain.usecase.habits.GetAllHabitsUseCase
import com.androidforge.habitflow.domain.usecase.habits.GetHabitByIdUseCase
import com.androidforge.habitflow.domain.usecase.habits.UpdateHabitUseCase
import com.androidforge.habitflow.domain.usecase.notification.CancelHabitReminderUseCase
import com.androidforge.habitflow.domain.usecase.notification.ScheduleHabitReminderUseCase
import com.androidforge.habitflow.domain.usecase.streaks.CalculateStreakUseCase

/**
 * Aggregates all use cases for easier injection and access in the presentation layer.
 */
data class DomainUseCases(
    val addHabit: AddHabitUseCase,
    val deleteHabit: DeleteHabitUseCase,
    val getAllHabits: GetAllHabitsUseCase,
    val getHabitById: GetHabitByIdUseCase,
    val updateHabit: UpdateHabitUseCase,
    val trackHabitCompletion: TrackHabitCompletionUseCase,
    val getHabitCompletionsForDate: GetHabitCompletionsForDateUseCase,
    val getHabitCompletionHistory: GetHabitCompletionHistoryUseCase,
    val calculateStreak: CalculateStreakUseCase,
    val scheduleHabitReminder: ScheduleHabitReminderUseCase,
    val cancelHabitReminder: CancelHabitReminderUseCase
)