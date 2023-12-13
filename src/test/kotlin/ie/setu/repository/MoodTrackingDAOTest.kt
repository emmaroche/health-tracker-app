package ie.setu.repository

import ie.setu.domain.MoodEntry
import ie.setu.domain.db.MoodTracking
import ie.setu.domain.repository.MoodTrackingDAO
import ie.setu.helpers.moodTracking
import ie.setu.helpers.populateMTTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val moodTracking1 = moodTracking[0]
private val moodTracking2 = moodTracking[1]
private val moodTracking3 = moodTracking[2]

class MoodTrackingDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadMoodTracking {

        @Test
        fun `getting all mood tracking records from a populated table returns all rows`() {
            transaction {
                populateUserTable() // Add this line to populate user records
                val moodTrackingDAO = populateMTTable()
                assertEquals(3, moodTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `get mood tracking records by user id that has no records, results in no records returned`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(0, moodTrackingDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get all mood tracking records over an empty table returns none`() {
            transaction {
                SchemaUtils.create(MoodTracking)
                val moodTrackingDAO = MoodTrackingDAO()
                assertEquals(0, moodTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `get mood tracking record by mood tracking id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(null, moodTrackingDAO.findById(4))
            }
        }

        @Test
        fun `get mood tracking record by mood tracking id that exists, results in a correct record returned`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(moodTracking3, moodTrackingDAO.findById(3))
                assertEquals(moodTracking2, moodTrackingDAO.findById(2))
            }
        }
    }

    @Nested
    inner class CreateMoodTracking {

        @Test
        fun `multiple mood tracking records added to the table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(3, moodTrackingDAO.getAll().size)
                assertEquals(moodTracking1, moodTrackingDAO.findById(moodTracking1.id))
                assertEquals(moodTracking2, moodTrackingDAO.findById(moodTracking2.id))
                assertEquals(moodTracking3, moodTrackingDAO.findById(moodTracking3.id))
            }
        }
    }

    @Nested
    inner class UpdateMoodTracking {

        @Test
        fun `updating an existing mood tracking record in the table results in a successful update`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                val updatedMoodTracking = MoodEntry(
                    id = 2,
                    date = DateTime.parse("2023-12-01T22:30:00"),
                    mood = "Joyful",
                    rating = 4,
                    notes = "Had a great day.",
                    userId = 1
                )
                moodTrackingDAO.updateMoodTracking(2, updatedMoodTracking)
                assertEquals(updatedMoodTracking, moodTrackingDAO.findById(2))
            }
        }

        @Test
        fun `updating a non-existent mood tracking record in the table results in no updates`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                val updatedMoodTracking = MoodEntry(
                    id = 4,
                    date = DateTime.parse("2023-12-01T22:30:00"),
                    mood = "Joyful",
                    rating = 4,
                    notes = "Had a great day.",
                    userId = 1
                )
                moodTrackingDAO.updateMoodTracking(4, updatedMoodTracking)
                assertEquals(null, moodTrackingDAO.findById(4))
                assertEquals(3, moodTrackingDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteMoodTracking {

        @Test
        fun `deleting a non-existent mood tracking record (by id) in the table results in no deletion`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(3, moodTrackingDAO.getAll().size)
                moodTrackingDAO.delete(4)
                assertEquals(3, moodTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing mood tracking record (by id) in the table results in the record being deleted`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(3, moodTrackingDAO.getAll().size)
                moodTrackingDAO.delete(moodTracking3.id)
                assertEquals(2, moodTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting mood tracking records when none exist for the user id results in no deletion`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(3, moodTrackingDAO.getAll().size)
                moodTrackingDAO.delete(4)
                assertEquals(3, moodTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting mood tracking records when 1 or more exist for the user id results in deletion`() {
            transaction {
                populateUserTable()
                val moodTrackingDAO = populateMTTable()
                assertEquals(3, moodTrackingDAO.getAll().size)
                moodTrackingDAO.delete(1)
                assertEquals(2, moodTrackingDAO.getAll().size)
            }
        }
    }
}
