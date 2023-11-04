package ie.setu.repository

import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.FitnessGoals
import ie.setu.domain.repository.FitnessGoalsDAO
import ie.setu.helpers.fitnessGoals
import ie.setu.helpers.populateFGTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val fitnessGoal1 = fitnessGoals[0]
private val fitnessGoal2 = fitnessGoals[1]
private val fitnessGoal3 = fitnessGoals[2]

class FitnessGoalsDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadFitnessGoals {

        @Test
        fun `getting all fitness goals from a populated table returns all rows`() {
            transaction {
                populateUserTable() // Add this line to populate user records
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(3, fitnessGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `get fitness goal by user id that has no fitness goals, results in no record returned`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(0, fitnessGoalsDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get all fitness goals over an empty table returns none`() {
            transaction {
                SchemaUtils.create(FitnessGoals)
                val fitnessGoalsDAO = FitnessGoalsDAO()
                assertEquals(0, fitnessGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `get fitness goal by fitness goal id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(null, fitnessGoalsDAO.findByType("Resistance training"))
            }
        }

        @Test
        fun `get fitness goal by fitness goal id that exists, results in a correct fitness goal returned`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(fitnessGoal3, fitnessGoalsDAO.findByGoalId(3))
                assertEquals(fitnessGoal2, fitnessGoalsDAO.findByGoalId(2))
            }
        }
    }

    @Nested
    inner class CreateFitnessGoals {

        @Test
        fun `multiple fitness goals added to the table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(3, fitnessGoalsDAO.getAll().size)
                assertEquals(fitnessGoal1, fitnessGoalsDAO.findByGoalId(fitnessGoal1.id))
                assertEquals(fitnessGoal2, fitnessGoalsDAO.findByGoalId(fitnessGoal2.id))
                assertEquals(fitnessGoal3, fitnessGoalsDAO.findByGoalId(fitnessGoal3.id))
            }
        }
    }

    @Nested
    inner class UpdateFitnessGoals {

        @Test
        fun `updating an existing fitness goal in the table results in a successful update`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                val updatedFitnessGoal = FitnessGoal(
                    id = 2,
                    type = "Cardio",
                    workoutsPerWeek = 4,
                    minutesOfWorkouts = 35,
                    calorieBurningGoalDuringExercise = 550.0,
                    userId = 2
                )
                fitnessGoalsDAO.updateFitnessGoal(updatedFitnessGoal.id, updatedFitnessGoal)
                assertEquals(updatedFitnessGoal, fitnessGoalsDAO.findByGoalId(2))
            }
        }

        @Test
        fun `updating a non-existent fitness goal in the table results in no updates`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                val updatedFitnessGoal = FitnessGoal(
                    id = 4,
                    type = "Cardio",
                    workoutsPerWeek = 4,
                    minutesOfWorkouts = 35,
                    calorieBurningGoalDuringExercise = 550.0,
                    userId = 2
                )
                fitnessGoalsDAO.updateFitnessGoal(4, updatedFitnessGoal)
                assertEquals(null, fitnessGoalsDAO.findByGoalId(4))
                assertEquals(3, fitnessGoalsDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteFitnessGoals {

        @Test
        fun `deleting a non-existent fitness goal (by id) in the table results in no deletion`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(3, fitnessGoalsDAO.getAll().size)
                fitnessGoalsDAO.delete(4)
                assertEquals(3, fitnessGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing fitness goal (by id) in the table results in the record being deleted`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(3, fitnessGoalsDAO.getAll().size)
                fitnessGoalsDAO.delete(fitnessGoal3.id)
                assertEquals(2, fitnessGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting fitness goals when none exist for the user id results in no deletion`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(3, fitnessGoalsDAO.getAll().size)
                fitnessGoalsDAO.delete(4)
                assertEquals(3, fitnessGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting fitness goals when 1 or more exist for the user id results in deletion`() {
            transaction {
                populateUserTable()
                val fitnessGoalsDAO = populateFGTable()
                assertEquals(3, fitnessGoalsDAO.getAll().size)
                fitnessGoalsDAO.delete(1)
                assertEquals(2, fitnessGoalsDAO.getAll().size)
            }
        }
    }
}
