package ie.setu.repository

import ie.setu.domain.WeightGoal
import ie.setu.domain.db.WeightGoals
import ie.setu.domain.repository.WeightGoalsDAO
import ie.setu.helpers.populateActivityTable
import ie.setu.helpers.weightGoals
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.populateWGTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val weightGoal1 = weightGoals[0]
private val weightGoal2 = weightGoals[1]
private val weightGoal3 = weightGoals[2]

class WeightGoalsDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadWeightGoals {

        @Test
        fun `getting all weight goals from a populated table returns all rows`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(3, weightGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `get weight goal by user id that has no weight goals, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(0, weightGoalsDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get all weight goals over an empty table returns none`() {
            transaction {
                SchemaUtils.create(WeightGoals)
                val weightGoalsDAO = WeightGoalsDAO()
                assertEquals(0, weightGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `get weight goal by weight goal id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(null, weightGoalsDAO.findByType("Maintenance"))
            }
        }

        @Test
        fun `get weight goal by weight goal id that exists, results in a correct weight goal returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(weightGoal3, weightGoalsDAO.findByGoalId(3))
                assertEquals(weightGoal2, weightGoalsDAO.findByGoalId(2))
            }
        }
    }

    @Nested
    inner class CreateWeightGoals {

        @Test
        fun `multiple weight goals added to the table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(3, weightGoalsDAO.getAll().size)
                assertEquals(weightGoal1, weightGoalsDAO.findByGoalId(weightGoal1.id))
                assertEquals(weightGoal2, weightGoalsDAO.findByGoalId(weightGoal2.id))
                assertEquals(weightGoal3, weightGoalsDAO.findByGoalId(weightGoal3.id))
            }
        }
    }

    @Nested
    inner class UpdateWeightGoals {

        @Test
        fun `updating an existing weight goal in the table results in a successful update`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                val updatedWeightGoal = WeightGoal(
                    id = 2,
                    type = "Weight Loss",
                    startingWeight = 180.0,
                    startingWeightTimestamp = weightGoal2.startingWeightTimestamp,
                    targetWeight = 170.0,
                    weeklyGoal = 0.3,
                    deadline = weightGoal2.deadline,
                    userId = 2,
                    actId = 1
                )
                weightGoalsDAO.updateWeightGoal(updatedWeightGoal.id, updatedWeightGoal)
                assertEquals(updatedWeightGoal, weightGoalsDAO.findByGoalId(2))
            }
        }

        @Test
        fun `updating a non-existent weight goal in the table results in no updates`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                val updatedWeightGoal = WeightGoal(
                    id = 4,
                    type = "Weight Gain",
                    startingWeight = 150.0,
                    startingWeightTimestamp = weightGoal1.startingWeightTimestamp,
                    targetWeight = 165.0,
                    weeklyGoal = 0.5,
                    deadline = weightGoal1.deadline,
                    userId = 1,
                    actId = 2
                )
                weightGoalsDAO.updateWeightGoal(4, updatedWeightGoal)
                assertEquals(null, weightGoalsDAO.findByGoalId(4))
                assertEquals(3, weightGoalsDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteWeightGoals {

        @Test
        fun `deleting a non-existent weight goal (by id) in the table results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(3, weightGoalsDAO.getAll().size)
                weightGoalsDAO.delete(4)
                assertEquals(3, weightGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing weight goal (by id) in the table results in the record being deleted`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(3, weightGoalsDAO.getAll().size)
                weightGoalsDAO.delete(weightGoal3.id)
                assertEquals(2, weightGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting weight goals when none exist for the user id results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(3, weightGoalsDAO.getAll().size)
                weightGoalsDAO.delete(4)
                assertEquals(3, weightGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting weight goals when 1 or more exist for the user id results in deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val weightGoalsDAO = populateWGTable()
                assertEquals(3, weightGoalsDAO.getAll().size)
                weightGoalsDAO.delete(1)
                assertEquals(2, weightGoalsDAO.getAll().size)
            }
        }
    }
}
