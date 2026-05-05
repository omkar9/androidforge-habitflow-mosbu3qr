package com.androidforge.habitflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androidforge.habitflow.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for HabitEntity operations.
 */
@Dao
interface HabitDao {

    /**
     * Inserts a new habit into the database.
     * If a habit with the same ID already exists, it will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHabit(habit: HabitEntity):
            Long // Returns the rowId of the inserted item, or -1 if ignored

    /**
     * Updates an existing habit in the database.
     */
    @Update
    suspend fun updateHabit(habit: HabitEntity)

    /**
     * Deletes a habit from the database.
     */
    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    /**
     * Retrieves a habit by its ID.
     * @param habitId The ID of the habit.
     * @return A Flow emitting the HabitEntity or null if not found.
     */
    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: String): Flow<HabitEntity?>

    /**
     * Retrieves all active habits, ordered by creation date.
     * @return A Flow emitting a list of HabitEntity.
     */
    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveHabits(): Flow<List<HabitEntity>>

    /**
     * Retrieves all habits (active or inactive).
     * @return A Flow emitting a list of HabitEntity.
     */
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    /**
     * Sets a habit's active status.
     * @param habitId The ID of the habit.
     * @param isActive The new active status.
     */
    @Query("UPDATE habits SET isActive = :isActive WHERE id = :habitId")
    suspend fun setHabitActiveStatus(habitId: String, isActive: Boolean)
}