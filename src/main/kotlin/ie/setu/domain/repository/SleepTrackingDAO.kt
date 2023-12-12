package ie.setu.domain.repository

import ie.setu.domain.SleepEntry
import ie.setu.domain.db.SleepTracking
import ie.setu.utils.mapToSleepTracking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class SleepTrackingDAO {

    // Get all sleep tracking entries
    fun getAll(): ArrayList<SleepEntry> {
        val stList: ArrayList<SleepEntry> = arrayListOf()
        transaction {
            SleepTracking.selectAll().map {
                stList.add(mapToSleepTracking(it))
            }
        }
        return stList
    }

    // Find sleep tracking entries by user ID
    fun findByUserId(userId: Int): List<SleepEntry> {
        return transaction {
            SleepTracking
                .select { SleepTracking.userId eq userId }
                .map { mapToSleepTracking(it) }
        }
    }

    // Find a specific sleep tracking entry by ID
    fun findById(id: Int): SleepEntry? {
        return transaction {
            SleepTracking
                .select { SleepTracking.id eq id }
                .map { mapToSleepTracking(it) }
                .firstOrNull()
        }
    }

    // Save sleep tracking entry
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

    // Delete sleep tracking entry
    fun delete(id: Int): Int {
        return transaction {
            SleepTracking.deleteWhere {
                SleepTracking.id eq id
            }
        }
    }

    // Update sleep tracking entry
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
