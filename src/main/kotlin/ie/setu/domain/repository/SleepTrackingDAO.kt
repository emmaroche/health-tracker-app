package ie.setu.domain.repository

import ie.setu.domain.SleepEntry
import ie.setu.domain.db.SleepTracking
import ie.setu.utils.mapToSleepTracking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `SleepTracking` table in the database.
 * It provides methods to perform CRUD operations on sleep tracking entries.
 */
class SleepTrackingDAO {

    /**
     * Get all sleep tracking entries from the database.
     *
     * @return List of sleep tracking entries.
     */
    fun getAll(): ArrayList<SleepEntry> {
        val stList: ArrayList<SleepEntry> = arrayListOf()
        transaction {
            SleepTracking.selectAll().map {
                stList.add(mapToSleepTracking(it))
            }
        }
        return stList
    }

    /**
     * Find sleep tracking entries by user ID.
     *
     * @param userId The ID of the user to find sleep tracking entries for.
     * @return List of sleep tracking entries for the specified user.
     */
    fun findByUserId(userId: Int): List<SleepEntry> {
        return transaction {
            SleepTracking
                .select { SleepTracking.userId eq userId }
                .map { mapToSleepTracking(it) }
        }
    }

    /**
     * Find a specific sleep tracking entry by ID.
     *
     * @param id The ID of the sleep tracking entry to find.
     * @return The found sleep tracking entry, or null if not found.
     */
    fun findById(id: Int): SleepEntry? {
        return transaction {
            SleepTracking
                .select { SleepTracking.id eq id }
                .map { mapToSleepTracking(it) }
                .firstOrNull()
        }
    }

    /**
     * Save sleep tracking entry to the database.
     *
     * @param sleepTracking The sleep tracking entry to save.
     * @return The ID of the saved sleep tracking entry.
     */
    fun save(sleepTracking: SleepEntry): Int {
        return transaction {
            SleepTracking.insert {
                it[date] = sleepTracking.date
                it[duration] = sleepTracking.duration
                it[quality] = sleepTracking.quality
                it[notes] = sleepTracking.notes
                it[userId] = sleepTracking.userId
            } get SleepTracking.id
        }
    }

    /**
     * Delete sleep tracking entry by ID.
     *
     * @param id The ID of the sleep tracking entry to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            SleepTracking.deleteWhere {
                SleepTracking.id eq id
            }
        }
    }

    /**
     * Update sleep tracking entry by ID.
     *
     * @param id The ID of the sleep tracking entry to update.
     * @param updatedSleepTracking The updated sleep tracking entry information.
     * @return The number of updated rows.
     */
    fun updateSleepTracking(id: Int, updatedSleepTracking: SleepEntry): Int {
        return transaction {
            SleepTracking.update({
                SleepTracking.id eq id
            }) {
                it[date] = updatedSleepTracking.date
                it[duration] = updatedSleepTracking.duration
                it[quality] = updatedSleepTracking.quality
                it[notes] = updatedSleepTracking.notes
                it[userId] = updatedSleepTracking.userId
            }
        }
    }
}
