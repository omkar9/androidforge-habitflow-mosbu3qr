package com.androidforge.habitflow.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

/**
 * Represents a habit in the domain layer.
 */
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val createdAt: LocalDate = LocalDate.now(),
    val frequency: HabitFrequency,
    val reminderTime: LocalTime? = null,
    val isActive: Boolean = true
)

/**
 * Defines how often a habit should be performed.
 */
sealed class HabitFrequency {
    object Daily : HabitFrequency()
    data class SpecificDays(val daysOfWeek: Set<DayOfWeek>) : HabitFrequency() // e.g., Monday, Wednesday, Friday
    data class Weekly(val timesPerWeek: Int) : HabitFrequency() // e.g., 3 times a week

    fun toShortString(): String {
        return when (this) {
            Daily -> "Daily"
            is SpecificDays -> daysOfWeek.sorted().joinToString(",") { it.name.take(3) }
            is Weekly -> "${timesPerWeek}x/Week"
        }
    }
}