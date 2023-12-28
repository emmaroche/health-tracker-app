package ie.setu.domain.repository

import ie.setu.domain.HealthRecord
import ie.setu.domain.User
import ie.setu.domain.db.HealthRecords
import ie.setu.utils.mapToHealthRecord
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `HealthRecords` table in the database.
 * It provides methods to perform CRUD operations on health record records.
 */
class HealthRecordDAO {

    /**
     * Get all health records from the database.
     *
     * @return List of health records.
     */
    fun getAll(): ArrayList<HealthRecord> {
        val hrList: ArrayList<HealthRecord> = arrayListOf()
        transaction {
            HealthRecords.selectAll().map {
                hrList.add(mapToHealthRecord(it))
            }
        }
        return hrList
    }

    /**
     * Find health records by user ID.
     *
     * @param userId The ID of the user to find health records for.
     * @return List of health records for the specified user.
     */
    fun findByUserId(userId: Int): List<HealthRecord> {
        return transaction {
            HealthRecords
                .select { HealthRecords.userId eq userId }
                .map { mapToHealthRecord(it) }
        }
    }

    /**
     * Find a specific health record by health record ID.
     *
     * @param id The ID of the health record to find.
     * @return The found health record, or null if not found.
     */
    fun findByHealthRecordId(id: Int): HealthRecord? {
        return transaction {
            HealthRecords
                .select { HealthRecords.id eq id }
                .map { mapToHealthRecord(it) }
                .firstOrNull()
        }
    }

    /**
     * Save health records to the database.
     *
     * @param healthRecord The health record to save.
     * @return The ID of the saved health record.
     */
    fun save(healthRecord: HealthRecord): Int {
        return transaction {
            HealthRecords.insert {
                it[timestamp] = healthRecord.timestamp
                it[firstName] = healthRecord.firstName
                it[lastName] = healthRecord.lastName
                it[sex] = healthRecord.sex
                it[dob] = healthRecord.dob
                it[height] = healthRecord.height
                it[bloodType] = healthRecord.bloodType
                it[allergies] = healthRecord.allergies
                it[medicalConditions] = healthRecord.medicalConditions
                it[medications] = healthRecord.medications
                it[notes] = healthRecord.notes
                it[userId] = healthRecord.userId
            } get HealthRecords.id
        }
    }

    /**
     * Find health records by user first name.
     *
     * @param firstName The first name of the user to find health records for.
     * @return The found user, or null if not found.
     */
    fun findByFirstName(firstName: String): User? {
        return transaction {
            HealthRecords.select {
                HealthRecords.firstName eq firstName
            }
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }

    /**
     * Delete a health record by health record ID.
     *
     * @param id The ID of the health record to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            HealthRecords.deleteWhere {
                HealthRecords.id eq id
            }
        }
    }

    /**
     * Update a health record by health record ID.
     *
     * @param healthId The ID of the health record to update.
     * @param healthRecordToUpdate The updated health record information.
     * @return The number of updated rows.
     */
    fun updateHealthRecord(healthId: Int, healthRecordToUpdate: HealthRecord): Int {
        return transaction {
            HealthRecords.update({
                HealthRecords.id eq healthId
            }) {
                it[timestamp] = healthRecordToUpdate.timestamp
                it[firstName] = healthRecordToUpdate.firstName
                it[lastName] = healthRecordToUpdate.lastName
                it[sex] = healthRecordToUpdate.sex
                it[dob] = healthRecordToUpdate.dob
                it[height] = healthRecordToUpdate.height
                it[bloodType] = healthRecordToUpdate.bloodType
                it[allergies] = healthRecordToUpdate.allergies
                it[medicalConditions] = healthRecordToUpdate.medicalConditions
                it[medications] = healthRecordToUpdate.medications
                it[notes] = healthRecordToUpdate.notes
                it[userId] = healthRecordToUpdate.userId
            }
        }
    }
}
