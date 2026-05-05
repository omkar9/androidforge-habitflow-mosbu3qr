package com.androidforge.habitflow.domain.usecase.notification

import android.content.Context
import androidx.work.WorkManager
import com.androidforge.habitflow.core.Constants
import javax.inject.Inject

/**
 * Use case for canceling scheduled habit reminders.
 */
class CancelHabitReminderUseCase @Inject constructor(
    private val context: Context
) {
    operator fun invoke(habitId: String) {
        WorkManager.getInstance(context).cancelUniqueWork("${Constants.WORK_NAME_HABIT_REMINDER}_${habitId}")
    }
}