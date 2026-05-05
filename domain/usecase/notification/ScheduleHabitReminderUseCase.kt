package com.androidforge.habitflow.domain.usecase.notification

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.androidforge.habitflow.core.Constants
import com.androidforge.habitflow.data.worker.HabitReminderWorker
import com.androidforge.habitflow.domain.model.Habit
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Use case for scheduling periodic reminders for habits using WorkManager.
 * Reminders are scheduled daily at the specified reminderTime.
 */
class ScheduleHabitReminderUseCase @Inject constructor(
    private val context: Context
) {
    operator fun invoke(habit: Habit) {
        habit.reminderTime ?: return // No reminder time, no scheduling

        val initialDelay = calculateInitialDelay(habit.reminderTime)

        // Constraints for the worker
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        // Data to pass to the worker
        val inputData = workDataOf(
            HabitReminderWorker.KEY_HABIT_ID to habit.id,
            HabitReminderWorker.KEY_HABIT_NAME to habit.name
        )

        // Schedule a periodic work request
        val reminderWorkRequest = PeriodicWorkRequestBuilder<
                HabitReminderWorker>(1, TimeUnit.DAYS) // Check daily
            .setInitialDelay(initialDelay.toMinutes(), TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(Constants.WORK_TAG_HABIT_REMINDER)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "${Constants.WORK_NAME_HABIT_REMINDER}_${habit.id}",
            ExistingPeriodicWorkPolicy.UPDATE, // Update existing work if habit is updated
            reminderWorkRequest
        )
    }

    /**
     * Calculates the initial delay until the next reminder time.
     * If the reminder time is in the past today, it schedules for tomorrow.
     */
    private fun calculateInitialDelay(reminderTime: LocalTime): Duration {
        val now = LocalTime.now()
        return if (reminderTime.isAfter(now)) {
            Duration.between(now, reminderTime)
        } else {
            // Reminder time has passed today, schedule for tomorrow
            Duration.between(now, LocalTime.MAX).plusMinutes(1) // Until end of day + 1 minute (start of next day)
                .plus(Duration.between(LocalTime.MIN, reminderTime)) // Plus time until reminder tomorrow
        }
    }
}