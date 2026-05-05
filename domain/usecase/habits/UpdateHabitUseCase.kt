package com.androidforge.habitflow.domain.usecase.habits

import com.androidforge.habitflow.domain.model.Habit
import com.androidforge.habitflow.domain.repository.HabitRepository
import javax.inject.Inject

/**
 * Use case for updating an existing habit's details.
 */
class UpdateHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        repository.updateHabit(habit)
    }
}