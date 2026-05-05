package com.androidforge.habitflow.domain.model

import java.time.LocalDate
import java.util.UUID

/**
 * Represents a record of a habit's completion status for a specific day.
 */
data class HabitCompletion(
    val id: String = UUID.randomUUID().toString(),
    val habitId: String,
    val date: LocalDate,
    val status: CompletionStatus
)

/**
 * Defines the completion status of a habit for a given day.
 */
enum class CompletionStatus {
    COMPLETED,
    SKIPPED,
    MISSED
}