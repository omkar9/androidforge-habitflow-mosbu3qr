package com.androidforge.habitflow.domain.usecase.habits

import com.androidforge.habitflow.domain.model.Habit
import com.androidforge.habitflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a single habit by its ID.
 */
class GetHabitByIdUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(habitId: String): Flow<Habit?> {
        return repository.getHabitById(habitId)
    }
}