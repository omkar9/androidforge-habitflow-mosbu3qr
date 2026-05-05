package com.androidforge.habitflow.domain.di

import android.content.Context
import com.androidforge.habitflow.domain.repository.HabitRepository
import com.androidforge.habitflow.domain.usecase.DomainUseCases
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing domain layer dependencies (Use Cases).
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideDomainUseCases(
        repository: HabitRepository,
        @ApplicationContext context: Context
    ): DomainUseCases {
        return DomainUseCases(
            addHabit = AddHabitUseCase(repository),
            deleteHabit = DeleteHabitUseCase(repository),
            getAllHabits = GetAllHabitsUseCase(repository),
            getHabitById = GetHabitByIdUseCase(repository),
            updateHabit = UpdateHabitUseCase(repository),
            trackHabitCompletion = TrackHabitCompletionUseCase(repository),
            getHabitCompletionsForDate = GetHabitCompletionsForDateUseCase(repository),
            getHabitCompletionHistory = GetHabitCompletionHistoryUseCase(repository),
            calculateStreak = CalculateStreakUseCase(repository),
            scheduleHabitReminder = ScheduleHabitReminderUseCase(context),
            cancelHabitReminder = CancelHabitReminderUseCase(context)
        )
    }
}