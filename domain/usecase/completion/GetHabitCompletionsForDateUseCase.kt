package com.androidforge.habitflow.domain.usecase.completion

import com.androidforge.habitflow.domain.model.HabitCompletion
import com.androidforge.habitflow.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for retrieving habit completion statuses for a given date.
 */
class GetHabitCompletionsForDateUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<HabitCompletion>> {
        return repository.getCompletionsByDate(date)
    }
}