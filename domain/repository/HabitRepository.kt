package com.androidforge.habitflow.domain.repository

import com.androidforge.habitflow.domain.model.CompletionStatus
import com.androidforge.habitflow.domain.model.Habit
import com.androidforge.habitflow.domain.model.HabitCompletion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Interface defining data operations for Habits and HabitCompletions.
 * This abstraction allows the domain layer to be independent of data source implementations.
 */
interface HabitRepository {

    // --- Habit Operations ---

    /**
     * Adds a new habit to the data source.
     * @param habit The Habit domain model to add.
     */
    suspend fun addHabit(habit: Habit)

    /**
     * Updates an existing habit in the data source.
     * @param habit The Habit domain model to update.
     */
    suspend fun updateHabit(habit: Habit)

    /**
     * Deletes a habit from the data source.
     * This should also delete all associated completion records.
     * @param habitId The ID of the habit to delete.
     */
    suspend fun deleteHabit(habitId: String)

    /**
     * Retrieves a habit by its ID.
     * @param habitId The ID of the habit.
     * @return A Flow emitting the Habit or null if not found.
     */
    fun getHabitById(habitId: String): Flow<Habit?>

    /**
     * Retrieves all active habits, ordered by creation date.
     * @return A Flow emitting a list of Habits.
     */
    fun getAllActiveHabits(): Flow<List<Habit>>

    /**
     * Retrieves all habits (active or inactive).
     * @return A Flow emitting a list of Habits.
     */
    fun getAllHabits(): Flow<List<Habit>>

    // --- Habit Completion Operations ---

    /**
     * Records or updates the completion status of a habit for a specific date.
     * If a record for the habit and date exists, it will be updated; otherwise, a new one is created.
     * @param habitId The ID of the habit.
     * @param date The date of completion.
     * @param status The CompletionStatus (COMPLETED, SKIPPED, MISSED).
     */
    suspend fun upsertHabitCompletion(habitId: String, date: LocalDate, status: CompletionStatus)

    /**
     * Retrieves the completion record for a specific habit on a given date.
     * @param habitId The ID of the habit.
     * @param date The date of the completion.
     * @return A Flow emitting the HabitCompletion or null if not found.
     */
    fun getHabitCompletionForDate(habitId: String, date: LocalDate): Flow<HabitCompletion?>

    /**
     * Retrieves the full completion history for a specific habit.
     * @param habitId The ID of the habit.
     * @return A Flow emitting a list of HabitCompletion, ordered by date ascending.
     */
    fun getHabitCompletionHistory(habitId: String): Flow<List<HabitCompletion>>

    /**
     * Retrieves all completion records for a specific date across all habits.
     * @param date The date to query.
     * @return A Flow emitting a list of HabitCompletion for the given date.
     */
    fun getCompletionsByDate(date: LocalDate): Flow<List<HabitCompletion>>
}