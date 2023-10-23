package ie.setu.domain

import org.joda.time.DateTime

data class HealthRecord(
    var id: Int,
//    val timestamp: DateTime,
    val firstName: String,
    val lastName: String,
    val sex: String,
//    val DOB: DateTime,
    val weight: Double,
//    val height: Int,
    val bloodType: String,
    val allergies: String,
    val medicalConditions: String,
    val medications: String,
    val notes: String,
    var userId: Int
)
