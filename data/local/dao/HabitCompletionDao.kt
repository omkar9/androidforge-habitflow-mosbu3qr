package com.androidforge.habitflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androidforge.habitflow.data.local.entity.HabitCompletionEntity
import com.androidforge.habitflow.domain.model.CompletionStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for HabitCompletionEntity operations.
 */
@Dao
interface HabitCompletionDao {

    /**
     * Inserts a new habit completion record or updates an existing one if a conflict occurs
     * (i.e., a completion for the same habit on the same date already exists).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHabitCompletion(completion: HabitCompletionEntity)

    /**
     * Updates an existing habit completion record.
     */
    @Update
    suspend fun updateHabitCompletion(completion: HabitCompletionEntity)

    /**
     * Retrieves a specific habit completion record by habit ID and date.
     * @param habitId The ID of the habit.
     * @param date The date of the completion.
     * @return A Flow emitting the HabitCompletionEntity or null if not found.
     */
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND date = :date")
    fun getHabitCompletionForDate(habitId: String, date: LocalDate): Flow<HabitCompletionEntity?>

    /**
     * Retrieves all completion records for a specific habit.
     * @param habitId The ID of the habit.
     * @return A Flow emitting a list of HabitCompletionEntity, ordered by date ascending.
     */
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY date ASC")
    fun getHabitCompletionHistory(habitId: String): Flow<List<HabitCompletionEntity>>

    /**
     * Retrieves all completion records for a specific date across all habits.
     * @param date The date to query.
     * @return A Flow emitting a list of HabitCompletionEntity for the given date.
     */
    @Query("SELECT * FROM habit_completions WHERE date = :date")
    fun getCompletionsByDate(date: LocalDate): Flow<List<HabitCompletionEntity>>

    /**
     * Deletes a specific habit completion record.
     */
    @Query("DELETE FROM habit_completions WHERE id = :completionId")
    suspend fun deleteHabitCompletion(completionId: String)

    /**
     * Deletes all completion records for a given habit.
     */
    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun deleteCompletionsForHabit(habitId: String)

    /**
     * Retrieves completion records for a habit within a specific date range.
     */
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getHabitCompletionsInDateRange(habitId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<HabitCompletionEntity>>

    /**
     * Retrieves the latest completion status for a habit up to a given date.
     */
    @Query("SELECT status FROM habit_completions WHERE habitId = :habitId AND date <= :date ORDER BY date DESC LIMIT 1")
    suspend fun getLatestCompletionStatusForHabit(habitId: String, date: LocalDate): CompletionStatus?
}