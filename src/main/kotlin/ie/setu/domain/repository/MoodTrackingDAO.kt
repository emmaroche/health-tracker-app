package ie.setu.domain.repository

import ie.setu.domain.MoodEntry
import ie.setu.domain.db.MoodTracking
import ie.setu.utils.mapToMoodTracking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `MoodTracking` table in the database.
 * It provides methods to perform CRUD operations on mood tracking entries.
 */
class MoodTrackingDAO {

    /**
     * Get all mood tracking entries from the database.
     *
     * @return List of mood tracking entries.
     */
    fun getAll(): ArrayList<MoodEntry> {
        val moodList: ArrayList<MoodEntry> = arrayListOf()
        transaction {
            MoodTracking.selectAll().map {
                moodList.add(mapToMoodTracking(it))
            }
        }
        return moodList
    }

    /**
     * Find mood tracking entries by user ID.
     *
     * @param userId The ID of the user to find mood tracking entries for.
     * @return List of mood tracking entries for the specified user.
     */
    fun findByUserId(userId: Int): List<MoodEntry> {
        return transaction {
            MoodTracking
                .select { MoodTracking.userId eq userId }
                .map { mapToMoodTracking(it) }
        }
    }

    /**
     * Find mood tracking entries by sleep ID.
     *
     * @param sleepId The ID of the sleep entry associated with mood tracking entries.
     * @return List of mood tracking entries associated with the specified sleep entry.
     */
    fun findBySleepId(sleepId: Int): List<MoodEntry> {
        return transaction {
            MoodTracking
                .select { MoodTracking.sleepId eq sleepId }
                .map { mapToMoodTracking(it) }
        }
    }

    /**
     * Find a specific mood tracking entry by ID.
     *
     * @param id The ID of the mood tracking entry to find.
     * @return The found mood tracking entry, or null if not found.
     */
    fun findById(id: Int): MoodEntry? {
        return transaction {
            MoodTracking
                .select { MoodTracking.id eq id }
                .map { mapToMoodTracking(it) }
                .firstOrNull()
        }
    }

    /**
     * Save mood tracking entry to the database.
     *
     * @param moodTracking The mood tracking entry to save.
     * @return The ID of the saved mood tracking entry.
     */
    fun save(moodTracking: MoodEntry): Int {
        return transaction {
            MoodTracking.insert {
                it[date] = moodTracking.date
                it[mood] = moodTracking.mood
                it[rating] = moodTracking.rating
                it[notes] = moodTracking.notes
                it[userId] = moodTracking.userId
                it[sleepId] = moodTracking.sleepId
            } get MoodTracking.id
        }
    }

    /**
     * Delete mood tracking entry by ID.
     *
     * @param id The ID of the mood tracking entry to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            MoodTracking.deleteWhere {
                MoodTracking.id eq id
            }
        }
    }

    /**
     * Update mood tracking entry by ID.
     *
     * @param id The ID of the mood tracking entry to update.
     * @param updatedMoodTracking The updated mood tracking entry information.
     * @return The number of updated rows.
     */
    fun updateMoodTracking(id: Int, updatedMoodTracking: MoodEntry): Int {
        return transaction {
            MoodTracking.update({
                MoodTracking.id eq id
            }) {
                it[date] = updatedMoodTracking.date
                it[mood] = updatedMoodTracking.mood
                it[rating] = updatedMoodTracking.rating
                it[notes] = updatedMoodTracking.notes
                it[userId] = updatedMoodTracking.userId
                it[sleepId] = updatedMoodTracking.sleepId
            }
        }
    }
}
