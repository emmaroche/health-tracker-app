package ie.setu.repository

import ie.setu.domain.NutritionGoal
import ie.setu.domain.db.NutritionGoals
import ie.setu.domain.repository.NutritionGoalsDAO
import ie.setu.helpers.nutritionGoals
import ie.setu.helpers.populateActivityTable
import ie.setu.helpers.populateNGTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val nutritionGoal1 = nutritionGoals[0]
private val nutritionGoal2 = nutritionGoals[1]
private val nutritionGoal3 = nutritionGoals[2]

class NutritionGoalsDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadNutritionGoals {

        @Test
        fun `getting all nutrition goals from a populated table returns all rows`() {
            transaction {
                populateUserTable()
                populateActivityTable() 
                populateActivityTable() 
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(3, nutritionGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `get nutrition goal by user id that has no nutrition goals, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(0, nutritionGoalsDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get all nutrition goals over an empty table returns none`() {
            transaction {
                SchemaUtils.create(NutritionGoals)
                val nutritionGoalsDAO = NutritionGoalsDAO()
                assertEquals(0, nutritionGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `get nutrition goal by nutrition goal id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(null, nutritionGoalsDAO.findByGoalId(100))
            }
        }

        @Test
        fun `get nutrition goal by nutrition goal id that exists, results in a correct nutrition goal returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(nutritionGoal3, nutritionGoalsDAO.findByGoalId(3))
                assertEquals(nutritionGoal2, nutritionGoalsDAO.findByGoalId(2))
            }
        }

        @Test
        fun `get nutrition goals by weight goal id that has no records, results in no records returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(0, nutritionGoalsDAO.findNutritionGoalsByWeightGoalId(4).size)
            }
        }

        @Test
        fun `get nutrition goals by weight goal id that exists, results in correct records returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()

                // Assuming that nutritionGoal1 and nutritionGoal2 have the same weightId
                assertEquals(1, nutritionGoalsDAO.findNutritionGoalsByWeightGoalId(nutritionGoal1.weightId).size)
                assertEquals(1, nutritionGoalsDAO.findNutritionGoalsByWeightGoalId(nutritionGoal3.weightId).size)
            }
        }

        @Test
        fun `get nutrition goals by fitness goal id that has no records, results in no records returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(0, nutritionGoalsDAO.findByFitnessId(4).size)
            }
        }

        @Test
        fun `get nutrition goals by fitness goal id that exists, results in correct records returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()

                assertEquals(1, nutritionGoalsDAO.findByFitnessId(nutritionGoal1.fitnessId).size)
                assertEquals(1, nutritionGoalsDAO.findByFitnessId(nutritionGoal3.fitnessId).size)
            }
        }
    }

    @Nested
    inner class CreateNutritionGoals {

        @Test
        fun `multiple nutrition goals added to the table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(3, nutritionGoalsDAO.getAll().size)
                assertEquals(nutritionGoal1, nutritionGoalsDAO.findByGoalId(nutritionGoal1.id))
                assertEquals(nutritionGoal2, nutritionGoalsDAO.findByGoalId(nutritionGoal2.id))
                assertEquals(nutritionGoal3, nutritionGoalsDAO.findByGoalId(nutritionGoal3.id))
            }
        }
    }

    @Nested
    inner class UpdateNutritionGoals {

        @Test
        fun `updating an existing nutrition goal in the table results in a successful update`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                val updatedNutritionGoal = NutritionGoal(
                    id = 2,
                    type = "NewType",
                    proteinGoal = 40.0,
                    fibreGoal = 10.0,
                    calorieGoal = 800.0,
                    carbsGoal = 30.0,
                    fatGoal = 20.0,
                    userId = 2,
                    fitnessId = 1,
                    weightId = 1
                )
                nutritionGoalsDAO.updateNutritionGoal(updatedNutritionGoal.id, updatedNutritionGoal)
                assertEquals(updatedNutritionGoal, nutritionGoalsDAO.findByGoalId(2))
            }
        }

        @Test
        fun `updating a non-existent nutrition goal in the table results in no updates`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                val updatedNutritionGoal = NutritionGoal(
                    id = 4,
                    type = "NewType",
                    proteinGoal = 40.0,
                    fibreGoal = 10.0,
                    calorieGoal = 800.0,
                    carbsGoal = 30.0,
                    fatGoal = 20.0,
                    userId = 2,
                    fitnessId = 3,
                    weightId = 2
                )
                nutritionGoalsDAO.updateNutritionGoal(4, updatedNutritionGoal)
                assertEquals(null, nutritionGoalsDAO.findByGoalId(4))
                assertEquals(3, nutritionGoalsDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteNutritionGoals {

        @Test
        fun `deleting a non-existent nutrition goal (by id) in the table results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(3, nutritionGoalsDAO.getAll().size)
                nutritionGoalsDAO.delete(4)
                assertEquals(3, nutritionGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing nutrition goal (by id) in the table results in the record being deleted`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(3, nutritionGoalsDAO.getAll().size)
                nutritionGoalsDAO.delete(nutritionGoal3.id)
                assertEquals(2, nutritionGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting nutrition goals when none exist for the user id results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(3, nutritionGoalsDAO.getAll().size)
                nutritionGoalsDAO.delete(4)
                assertEquals(3, nutritionGoalsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting nutrition goals when 1 or more exist for the user id results in deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val nutritionGoalsDAO = populateNGTable()
                assertEquals(3, nutritionGoalsDAO.getAll().size)
                nutritionGoalsDAO.delete(1)
                assertEquals(2, nutritionGoalsDAO.getAll().size)
            }
        }
    }
}
