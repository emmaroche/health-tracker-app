package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * Represents the database table for storing health records.
 */
object HealthRecords : Table("healthRecords") {
    val id = integer("id").autoIncrement().primaryKey()
    val timestamp = datetime("timestamp")
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val sex = varchar("sex", 10)
    val dob = datetime("dob")
    val height = integer("height")
    val bloodType = varchar("blood_type", 5)
    val allergies = varchar("allergies", 100)
    val medicalConditions = varchar("medical_conditions", 100)
    val medications = varchar("medications", 100)
    val notes = varchar("notes", 200)

    // Reference to the Users table
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}
