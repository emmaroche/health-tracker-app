package ie.setu.repository

import ie.setu.domain.SleepEntry
import ie.setu.domain.db.SleepTracking
import ie.setu.domain.repository.SleepTrackingDAO
import ie.setu.helpers.populateSTTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.sleepTracking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val sleepTracking1 = sleepTracking[0]
private val sleepTracking2 = sleepTracking[1]
private val sleepTracking3 = sleepTracking[2]

class SleepTrackingDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadSleepTracking {

        @Test
        fun `getting all sleep tracking records from a populated table returns all rows`() {
            transaction {
                populateUserTable() // Add this line to populate user records
                val sleepTrackingDAO = populateSTTable()
                assertEquals(3, sleepTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `get sleep tracking records by user id that has no records, results in no records returned`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(0, sleepTrackingDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get all sleep tracking records over an empty table returns none`() {
            transaction {
                SchemaUtils.create(SleepTracking)
                val sleepTrackingDAO = SleepTrackingDAO()
                assertEquals(0, sleepTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `get sleep tracking record by sleep tracking id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(null, sleepTrackingDAO.findById(4))
            }
        }

        @Test
        fun `get sleep tracking record by sleep tracking id that exists, results in a correct record returned`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(sleepTracking3, sleepTrackingDAO.findById(3))
                assertEquals(sleepTracking2, sleepTrackingDAO.findById(2))
            }
        }
    }

    @Nested
    inner class CreateSleepTracking {

        @Test
        fun `multiple sleep tracking records added to the table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(3, sleepTrackingDAO.getAll().size)
                assertEquals(sleepTracking1, sleepTrackingDAO.findById(sleepTracking1.id))
                assertEquals(sleepTracking2, sleepTrackingDAO.findById(sleepTracking2.id))
                assertEquals(sleepTracking3, sleepTrackingDAO.findById(sleepTracking3.id))
            }
        }
    }

    @Nested
    inner class UpdateSleepTracking {

        @Test
        fun `updating an existing sleep tracking record in the table results in a successful update`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                val updatedSleepTracking = SleepEntry(
                    id = 2,
                    date = DateTime.parse("2023-12-01T22:30:00"),
                    duration = 480,
                    quality = "Good",
                    notes = "Had a relaxing night.",
                    userId = 1
                )
                sleepTrackingDAO.updateSleepTracking(2, updatedSleepTracking)
                assertEquals(updatedSleepTracking, sleepTrackingDAO.findById(2))
            }
        }

        @Test
        fun `updating a non-existent sleep tracking record in the table results in no updates`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                val updatedSleepTracking = SleepEntry(
                    id = 4,
                    date = DateTime.parse("2023-12-01T22:30:00"),
                    duration = 480,
                    quality = "Good",
                    notes = "Had a relaxing night.",
                    userId = 1
                )
                sleepTrackingDAO.updateSleepTracking(4, updatedSleepTracking)
                assertEquals(null, sleepTrackingDAO.findById(4))
                assertEquals(3, sleepTrackingDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteSleepTracking {

        @Test
        fun `deleting a non-existent sleep tracking record (by id) in the table results in no deletion`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(3, sleepTrackingDAO.getAll().size)
                sleepTrackingDAO.delete(4)
                assertEquals(3, sleepTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing sleep tracking record (by id) in the table results in the record being deleted`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(3, sleepTrackingDAO.getAll().size)
                sleepTrackingDAO.delete(sleepTracking3.id)
                assertEquals(2, sleepTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting sleep tracking records when none exist for the user id results in no deletion`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(3, sleepTrackingDAO.getAll().size)
                sleepTrackingDAO.delete(4)
                assertEquals(3, sleepTrackingDAO.getAll().size)
            }
        }

        @Test
        fun `deleting sleep tracking records when 1 or more exist for the user id results in deletion`() {
            transaction {
                populateUserTable()
                val sleepTrackingDAO = populateSTTable()
                assertEquals(3, sleepTrackingDAO.getAll().size)
                sleepTrackingDAO.delete(1)
                assertEquals(2, sleepTrackingDAO.getAll().size)
            }
        }
    }
}
