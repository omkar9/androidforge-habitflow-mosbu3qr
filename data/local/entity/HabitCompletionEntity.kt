package com.androidforge.habitflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.androidforge.habitflow.domain.model.CompletionStatus
import java.time.LocalDate

/**
 * Room entity for storing habit completion records.
 * Uses a composite unique index on `habitId` and `date` to prevent duplicate entries.
 */
@Entity(
    tableName = "habit_completions",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["habitId", "date"], unique = true)]
)
data class HabitCompletionEntity(
    @PrimaryKey
    val id: String,
    val habitId: String,
    val date: LocalDate,
    val status: CompletionStatus
)