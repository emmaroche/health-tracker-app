package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
object MoodTracking : Table("moodTracking") {
    val id = integer("id").autoIncrement().primaryKey()
    val date = datetime("date")
    val mood = varchar("mood", 50)
    val rating = integer("rating")
    val notes = varchar("notes", 200)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}
