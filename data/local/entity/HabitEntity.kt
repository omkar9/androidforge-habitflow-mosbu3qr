package com.androidforge.habitflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidforge.habitflow.domain.model.HabitFrequency
import java.time.LocalDate
import java.time.LocalTime

/**
 * Room entity for storing habit data.
 * The `id` is a UUID string to allow for easier future integration with remote databases if needed.
 */
@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val createdAt: LocalDate,
    val frequency: HabitFrequency,
    val reminderTime: LocalTime?,
    val isActive: Boolean
)