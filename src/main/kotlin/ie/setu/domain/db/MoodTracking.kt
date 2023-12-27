package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * Represents the database table for storing mood tracking entries.
 */
object MoodTracking : Table("moodTracking") {
    val id = integer("id").autoIncrement().primaryKey()
    val date = datetime("date")
    val mood = varchar("mood", 50)
    val rating = integer("rating")
    val notes = varchar("notes", 200)

    // Reference to the Users table
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    // Reference to the SleepTracking table, nullable
    val sleepId = integer("sleep_id").references(SleepTracking.id, onDelete = ReferenceOption.SET_NULL).nullable()
}
