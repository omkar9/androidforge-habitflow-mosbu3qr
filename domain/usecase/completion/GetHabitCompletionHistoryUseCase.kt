package com.androidforge.habitflow.domain.usecase.completion

import com.androidforge.habitflow.domain.model.HabitCompletion
import com.androidforge.habitflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the full completion history of a habit.
 */
class GetHabitCompletionHistoryUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(habitId: String): Flow<List<HabitCompletion>> {
        return repository.getHabitCompletionHistory(habitId)
    }
}