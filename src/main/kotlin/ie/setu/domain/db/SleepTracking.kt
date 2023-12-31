package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * Represents the database table for storing sleep tracking entries.
 */
object SleepTracking : Table("sleepTracking") {
    val id = integer("id").autoIncrement().primaryKey()
    val date = datetime("date")
    val duration = integer("duration")
    val quality = varchar("quality", 50)
    val notes = varchar("notes", 200)

    // Reference to the Users table
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}
