package ie.setu.utils

import ie.setu.domain.User
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow
import ie.setu.domain.Activity
import ie.setu.domain.HealthRecord
import ie.setu.domain.db.HealthRecords
import ie.setu.domain.db.Activities

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToHealthRecord(it: ResultRow) = HealthRecord(
    id = it[HealthRecords.id],
//    timestamp = it[HealthRecords.timestamp],
    firstName = it[HealthRecords.firstName],
    lastName = it[HealthRecords.lastName],
    sex = it[HealthRecords.sex],
//    DOB = it[HealthRecords.DOB],
    weight = it[HealthRecords.weight],
//    height = it[HealthRecords.height],
    bloodType = it[HealthRecords.bloodType],
    allergies = it[HealthRecords.allergies],
    medicalConditions = it[HealthRecords.medicalConditions],
    medications = it[HealthRecords.medications],
    notes = it[HealthRecords.notes],
    userId = it[HealthRecords.userId]
)
