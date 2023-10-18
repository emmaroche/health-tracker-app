package ie.setu.domain.repository

import ie.setu.domain.HealthRecord
import ie.setu.domain.User
import ie.setu.domain.db.HealthRecords
import ie.setu.utils.mapToHealthRecord
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class HealthRecordDAO {

    // Get all health records
    fun getAll(): ArrayList<HealthRecord> {
        val hrList: ArrayList<HealthRecord> = arrayListOf()
        transaction {
            HealthRecords.selectAll().map {
                hrList.add(mapToHealthRecord(it)) }
        }
        return hrList
    }

    // Find health record by user ID
    fun findByUserId(userId: Int): List<HealthRecord>{
        return transaction {
            HealthRecords
                .select { HealthRecords.userId eq userId}
                .map { mapToHealthRecord(it) }
        }
    }

    // Save health records
    fun save(healthRecord: HealthRecord): Int {
        return transaction {
            HealthRecords.insert {
//                it[timestamp] = healthRecord.timestamp
                it[firstName] = healthRecord.firstName
                it[lastName] = healthRecord.lastName
                it[sex] = healthRecord.sex
//                it[DOB] = healthRecord.DOB
                it[weight] = healthRecord.weight
//                it[height] = healthRecord.height
//                it[bloodType] = healthRecord.bloodType
//                it[allergies] = healthRecord.allergies
//                it[medicalConditions] = healthRecord.medicalConditions
//                it[medications] = healthRecord.medications
//                it[notes] = healthRecord.notes
                it[userId] = healthRecord.userId
            } get HealthRecords.id
        }
    }



    // Find health records by user first name
    fun findByFirstName(firstName: String) : User?{
        return transaction {
            HealthRecords.select {
                HealthRecords.firstName eq firstName}
                .map{ mapToUser(it) }
                .firstOrNull()
        }
    }

    // Delete health record

    fun delete(id: Int):Int{
        return transaction{ HealthRecords.deleteWhere{
            HealthRecords.id eq id
        }
        }
    }

    // Update health record

    fun updateHealthRecord(id: Int, healthRecord: HealthRecord): Int {
        return transaction {
            HealthRecords.update({
                HealthRecords.id eq id
            }) {
//                it[timestamp] = healthRecord.timestamp
                it[firstName] = healthRecord.firstName
                it[lastName] = healthRecord.lastName
                it[sex] = healthRecord.sex
//                it[DOB] = healthRecord.DOB
                it[weight] = healthRecord.weight
                it[height] = healthRecord.height
                it[bloodType] = healthRecord.bloodType
                it[allergies] = healthRecord.allergies
                it[medicalConditions] = healthRecord.medicalConditions
                it[medications] = healthRecord.medications
                it[notes] = healthRecord.notes
                it[userId] = healthRecord.userId
            }
        }
    }



}


