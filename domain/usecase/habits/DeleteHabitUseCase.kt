package com.androidforge.habitflow.domain.usecase.habits

import com.androidforge.habitflow.domain.repository.HabitRepository
import javax.inject.Inject

/**
 * Use case for deleting an existing habit.
 * This performs a soft delete by setting `isActive` to false.
 */
class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: String) {
        repository.deleteHabit(habitId)
    }
}