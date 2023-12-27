package ie.setu.domain

import org.joda.time.DateTime

/**
 * Represents a health record for a user, containing various health-related information.
 *
 * @property id Unique identifier for the health record.
 * @property timestamp The date and time when the health record was created or last updated.
 * @property firstName The first name of the user.
 * @property lastName The last name of the user.
 * @property sex The gender/sex of the user.
 * @property dob The date of birth of the user.
 * @property weight The current weight of the user.
 * @property height The height of the user.
 * @property bloodType The blood type of the user.
 * @property allergies Any allergies the user may have.
 * @property medicalConditions Any existing medical conditions of the user.
 * @property medications Any medications the user is currently taking.
 * @property notes Additional notes or comments related to the health record.
 * @property userId The ID of the user to whom the health record belongs.
 */
data class HealthRecord(
    var id: Int,
    val timestamp: DateTime,
    val firstName: String,
    val lastName: String,
    val sex: String,
    val dob: DateTime,
    val weight: Double,
    val height: Int,
    val bloodType: String,
    val allergies: String,
    val medicalConditions: String,
    val medications: String,
    val notes: String,
    var userId: Int
)
