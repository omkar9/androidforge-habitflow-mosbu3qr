package com.androidforge.habitflow.domain.usecase.completion

import com.androidforge.habitflow.domain.model.CompletionStatus
import com.androidforge.habitflow.domain.repository.HabitRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for marking a habit as completed, skipped, or missed for a specific day.
 */
class TrackHabitCompletionUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(
        habitId: String,
        date: LocalDate,
        status: CompletionStatus
    ) {
        repository.upsertHabitCompletion(habitId, date, status)
    }
}