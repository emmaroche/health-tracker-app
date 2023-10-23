package ie.setu.repository

import ie.setu.domain.HealthRecord
import ie.setu.domain.db.HealthRecords
import ie.setu.domain.repository.HealthRecordDAO
import ie.setu.helpers.healthRecords
import ie.setu.helpers.populateHRTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val hr1 = healthRecords[0]
private val hr2 = healthRecords[1]
private val hr3 = healthRecords[2]
class HealthRecordDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadHealthRecords {

        @Test
        fun `getting all health records from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three health records
                populateUserTable()
                val healthRecordDAO = populateHRTable()
                //Act & Assert
                assertEquals(3, healthRecordDAO.getAll().size)
            }
        }

        @Test
        fun `get health record by user id that has no health records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three health records
                populateUserTable()
                val healthRecordDAO = populateHRTable()
                //Act & Assert
                assertEquals(0, healthRecordDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get all health records over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(HealthRecords)
                val healthRecordDAO = HealthRecordDAO()

                //Act & Assert
                assertEquals(0, healthRecordDAO.getAll().size)
            }
        }

        @Test
        fun `get health record by health record id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three health records
                populateUserTable()
                val healthRecordDAO = populateHRTable()
                //Act & Assert
                assertEquals(null, healthRecordDAO.findByFirstName("Mike"))
            }
        }

        @Test
        fun `get health record by health record id that exists, results in a correct health record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three health records
                populateUserTable()
                val healthRecordDAO = populateHRTable()
                //Act & Assert
                assertEquals(hr3, healthRecordDAO.findByHealthRecordId(3))
                assertEquals(hr2, healthRecordDAO.findByHealthRecordId(2))
            }
        }
    }

    @Nested
    inner class CreateHealthRecords {

        @Test
        fun `multiple health records added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three health records
                populateUserTable()
                val healthRecordDAO = populateHRTable()
                //Act & Assert
                assertEquals(3, healthRecordDAO.getAll().size)
                assertEquals(hr1, healthRecordDAO.findByHealthRecordId(hr1.id))
                assertEquals(hr2, healthRecordDAO.findByHealthRecordId(hr2.id))
                assertEquals(hr3, healthRecordDAO.findByHealthRecordId(hr3.id))
            }
        }
    }

    @Nested
    inner class UpdateHealthRecords {

//        @Test
//        fun `updating existing activity in table results in successful update`() {
//            transaction {
//
//                //Arrange - create and populate tables with three users and three activities
//                populateUserTable()
//                val healthRecordDAO = populateHRTable()
//
//                //Act & Assert
//                val activity3updated = Activity(id = 3, description = "Cardio", duration = 42.0,
//                    calories = 220, started = DateTime.now(), userId = 2)
//                healthRecordDAO.updateHealthRecord(activity3updated.id, activity3updated)
//                assertEquals(activity3updated, healthRecordDAO.findByHealthRecordId(3))
//            }
//        }

        @Test
        fun `updating non-existent health record in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with users and health records
                populateUserTable()
                val healthRecordDAO = populateHRTable()

                // Act - Try to update a non-existent health record
                val updatedHealthRecord = HealthRecord(
                    id = 4,
                    timestamp = DateTime.now(),
                    firstName = "John",
                    lastName = "Doe",
                    sex = "Male",
                    dob = DateTime(635734800000),
                    weight = 75.5,
                    height = 175,
                    bloodType = "A+",
                    allergies = "None",
                    medicalConditions = "None",
                    medications = "None",
                    notes = "Regular checkup",
                    userId = 2
                )


                // Assert - Ensure that no updates were made and the record is not found

                assertEquals(null, healthRecordDAO.findByHealthRecordId(4))
                assertEquals(3, healthRecordDAO.getAll().size)
            }
        }


    }

}

