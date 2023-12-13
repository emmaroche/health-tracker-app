package ie.setu.domain.repository

import ie.setu.domain.MoodEntry
import ie.setu.domain.db.MoodTracking
import ie.setu.utils.mapToMoodTracking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MoodTrackingDAO {

    // Get all mood tracking entries
    fun getAll(): ArrayList<MoodEntry> {
        val moodList: ArrayList<MoodEntry> = arrayListOf()
        transaction {
            MoodTracking.selectAll().map {
                moodList.add(mapToMoodTracking(it))
            }
        }
        return moodList
    }

    // Find mood tracking entries by user ID
    fun findByUserId(userId: Int): List<MoodEntry> {
        return transaction {
            MoodTracking
                .select { MoodTracking.userId eq userId }
                .map { mapToMoodTracking(it) }
        }
    }

    // Find a specific mood tracking entry by ID
    fun findById(id: Int): MoodEntry? {
        return transaction {
            MoodTracking
                .select { MoodTracking.id eq id }
                .map { mapToMoodTracking(it) }
                .firstOrNull()
        }
    }

    // Save mood tracking entry
    fun save(moodTracking: MoodEntry): Int {
        return transaction {
            MoodTracking.insert {
                it[date] = moodTracking.date
                it[mood] = moodTracking.mood
                it[rating] = moodTracking.rating
                it[notes] = moodTracking.notes
                it[userId] = moodTracking.userId
            } get MoodTracking.id
        }
    }

    // Delete mood tracking entry
    fun delete(id: Int): Int {
        return transaction {
            MoodTracking.deleteWhere {
                MoodTracking.id eq id
            }
        }
    }

    // Update mood tracking entry
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
            }
        }
    }
}
