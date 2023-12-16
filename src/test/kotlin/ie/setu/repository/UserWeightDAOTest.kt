package ie.setu.repository

import ie.setu.domain.CurrentWeight
import ie.setu.domain.db.UserWeight
import ie.setu.domain.repository.UserWeightDAO
import ie.setu.helpers.populateUWTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.userWeight
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private val currentWeight1 = userWeight[0]
private val currentWeight2 = userWeight[1]
private val currentWeight3 = userWeight[2]

class UserWeightDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadCurrentWeights {

        @Test
        fun `getting all current weights from a populated table returns all rows`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(3, userWeightDAO.getAll().size)
            }
        }

        @Test
        fun `get current weights by user id that has no current weights, results in no record returned`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(0, userWeightDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get all current weights over an empty table returns none`() {
            transaction {
                SchemaUtils.create(UserWeight)
                val userWeightDAO = UserWeightDAO()
                assertEquals(0, userWeightDAO.getAll().size)
            }
        }

        @Test
        fun `get current weight by weight id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertNull(userWeightDAO.findByWeightId(4))
            }
        }
        @Test
        fun `get current weight by weight id that exists, results in a correct current weight returned`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(currentWeight3, userWeightDAO.findByWeightId(3))
                assertEquals(currentWeight2, userWeightDAO.findByWeightId(2))
            }
        }
    }

    @Nested
    inner class CreateCurrentWeights {

        @Test
        fun `multiple current weights added to the table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(3, userWeightDAO.getAll().size)
                assertEquals(currentWeight1, userWeightDAO.findByWeightId(currentWeight1.id))
                assertEquals(currentWeight2, userWeightDAO.findByWeightId(currentWeight2.id))
                assertEquals(currentWeight3, userWeightDAO.findByWeightId(currentWeight3.id))
            }
        }
    }

    @Nested
    inner class UpdateCurrentWeights {

        @Test
        fun `updating an existing current weight in the table results in a successful update`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                val updatedCurrentWeight = CurrentWeight(
                    id = 2,
                    currentWeight = 70.0,
                    currentWeightTimestamp = currentWeight2.currentWeightTimestamp,
                    weightGoalId = 2,
                    userId = 2
                )
                userWeightDAO.updateCurrentWeight(updatedCurrentWeight.id, updatedCurrentWeight)
                assertEquals(updatedCurrentWeight, userWeightDAO.findByWeightId(2))
            }
        }

        @Test
        fun `updating a non-existent current weight in the table results in no updates`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                val updatedCurrentWeight = CurrentWeight(
                    id = 4,
                    currentWeight = 75.0,
                    currentWeightTimestamp = currentWeight1.currentWeightTimestamp,
                    weightGoalId = 1,
                    userId = 1
                )
                userWeightDAO.updateCurrentWeight(4, updatedCurrentWeight)
                assertEquals(null, userWeightDAO.findByWeightId(4))
                assertEquals(3, userWeightDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteCurrentWeights {

        @Test
        fun `deleting a non-existent current weight (by id) in the table results in no deletion`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(3, userWeightDAO.getAll().size)
                userWeightDAO.delete(4)
                assertEquals(3, userWeightDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing current weight (by id) in the table results in the record being deleted`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(3, userWeightDAO.getAll().size)
                userWeightDAO.delete(currentWeight3.id)
                assertEquals(2, userWeightDAO.getAll().size)
            }
        }

        @Test
        fun `deleting current weights when none exist for the user id results in no deletion`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(3, userWeightDAO.getAll().size)
                userWeightDAO.delete(4)
                assertEquals(3, userWeightDAO.getAll().size)
            }
        }

        @Test
        fun `deleting current weights when 1 or more exist for the user id results in deletion`() {
            transaction {
                populateUserTable()
                val userWeightDAO = populateUWTable()
                assertEquals(3, userWeightDAO.getAll().size)
                userWeightDAO.delete(1)
                assertEquals(2, userWeightDAO.getAll().size)
            }
        }
    }
}
