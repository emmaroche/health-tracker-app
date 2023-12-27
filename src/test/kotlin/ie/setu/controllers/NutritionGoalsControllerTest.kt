package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.NutritionGoal
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.TestUtilities
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NutritionGoalsControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    private val testUtilities = TestUtilities()

    @AfterEach
    fun tearDown() {
        val emailToDelete = "testuser1@test.com"
        val userToDeleteResponse = testUtilities.retrieveUserByEmail(emailToDelete)
        if (userToDeleteResponse.status == 200) {
            val responseBody = userToDeleteResponse.body.toString()
            val objectMapper = ObjectMapper()
            val jsonResponse = objectMapper.readTree(responseBody)
            val userId = jsonResponse["id"].asInt()
            testUtilities.deleteUser(userId)
        }
    }

    @Nested
    inner class ReadNutritionGoals {

        @Test
        fun `get all nutrition goals from the database returns 200 or 404 response`() {
            val response = retrieveAllNutritionGoals()
            if (response.status == 200) {
                val retrievedNutritionGoals = jsonNodeToObject<Array<NutritionGoal>>(response)
                assertNotEquals(0, retrievedNutritionGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all nutrition goals by user id when user and nutrition goals exist returns 200 response`() {
            // Arrange - add a user and 3 associated nutrition goals that we plan to retrieve
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val fitnessId = 174
            val weightId = 304

            addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )
            addNutritionGoal(
                nutritionGoals[1].type, nutritionGoals[1].proteinGoal,
                nutritionGoals[1].fibreGoal, nutritionGoals[1].calorieGoal, nutritionGoals[1].carbsGoal,
                nutritionGoals[1].fatGoal, addedUser.id, fitnessId, weightId
            )
            addNutritionGoal(
                nutritionGoals[2].type, nutritionGoals[2].proteinGoal,
                nutritionGoals[2].fibreGoal, nutritionGoals[2].calorieGoal, nutritionGoals[2].carbsGoal,
                nutritionGoals[2].fatGoal, addedUser.id, fitnessId, weightId
            )

            // Act and Assert - retrieve the three added nutrition goals by user id
            val response = retrieveNutritionGoalsByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedNutritionGoals = jsonNodeToObject<Array<NutritionGoal>>(response)
            assertEquals(3, retrievedNutritionGoals.size)

            // After - delete the added user and assert a 204 is returned (nutrition goals are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all nutrition goals by user id when no nutrition goals exist returns 404 response`() {
            // Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            // Act and Assert - retrieve the nutrition goals by user id
            val response = retrieveNutritionGoalsByUserId(addedUser.id)
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all nutrition goals by user id when no user exists returns 404 response`() {
            // Arrange
            val userId = -1

            // Act and Assert - retrieve nutrition goals by user id
            val response = retrieveNutritionGoalsByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get nutrition goal by goal id when no nutrition goal exists returns 404 response`() {
            // Arrange
            val goalId = -1

            // Act and Assert - attempt to retrieve the nutrition goal by goal id
            val response = retrieveNutritionGoalByGoalId(goalId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get nutrition goal by goal id when nutrition goal exists returns 200 response`() {
            // Arrange - add a user and an associated nutrition goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val fitnessId = 174
            val weightId = 304

            val addGoalResponse = addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )
            assertEquals(201, addGoalResponse.status)
            val addedGoal = jsonNodeToObject<NutritionGoal>(addGoalResponse)

            // Act and Assert - retrieve the nutrition goal by goal id
            val response = retrieveNutritionGoalByGoalId(addedGoal.id)
            assertEquals(200, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get nutrition goals by weight goal id when weight goal exists returns 200 response`() {
            // Arrange - add a weight goal, a user, and an associated nutrition goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val fitnessId = 174
            val weightId = 304

            addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )

            // Act and Assert - retrieve nutrition goals by weight goal id
            val response = retrieveNutritionGoalsByWeightGoalId(weightId)
            assertEquals(200, response.status)
            val retrievedNutritionGoals = jsonNodeToObject<Array<NutritionGoal>>(response)
            assertNotEquals(0, retrievedNutritionGoals.size)

            // After - delete the added user and assert a 204 is returned (weight goal and nutrition goal are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get nutrition goals by weight goal id when weight goal doesn't exist returns 404 response`() {
            // Act and Assert - retrieve nutrition goals by weight goal id
            val response = retrieveNutritionGoalsByWeightGoalId(-1)
            assertEquals(404, response.status)
        }

        @Test
        fun `get nutrition goals by fitness goal id when fitness goal exists returns 200 response`() {
            // Arrange - add a fitness goal, a user, and an associated nutrition goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val fitnessId = 174
            val weightId = 304

            addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )

            // Act and Assert - retrieve nutrition goals by fitness goal id
            val response = retrieveNutritionGoalsByFitnessId(fitnessId)
            assertEquals(200, response.status)
            val retrievedNutritionGoals = jsonNodeToObject<Array<NutritionGoal>>(response)
            assertNotEquals(0, retrievedNutritionGoals.size)

            // After - delete the added user and assert a 204 is returned (fitness goal and nutrition goal are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get nutrition goals by fitness goal id when fitness goal doesn't exist returns 404 response`() {
            // Act and Assert - retrieve nutrition goals by fitness goal id
            val response = retrieveNutritionGoalsByFitnessId(-1)
            assertEquals(404, response.status)
        }


    }

    @Nested
    inner class CreateNutritionGoals {

        @Test
        fun `add a nutrition goal when a user exists for it, returns a 201 response`() {
            // Arrange - add a user and an associated nutrition goal that we plan to do a delete on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val fitnessId = 174
            val weightId = 304

            val addGoalResponse = addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )
            assertEquals(201, addGoalResponse.status)

            // After - delete the user (nutrition goal will cascade delete in the database)
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add a nutrition goal when no user exists for it, returns a 404 response`() {

            // Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            val fitnessId = 174
            val weightId = 304

            // Add fitnessId to the addNutritionGoal call
            val addGoalResponse = addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, userId, fitnessId, weightId
            )

            assertEquals(404, addGoalResponse.status)
        }


    }

    @Nested
    inner class UpdateNutritionGoals {

        @Test
        fun `updating a nutrition goal by goal id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val goalId = -1

            // Arrange - check there is no user for -1 id
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            val fitnessId = 174
            val weightId = 304

            // Act and Assert - attempt to update the details of a nutrition goal/user that doesn't exist
            assertEquals(
                404, updateNutritionGoal(
                    goalId, updatedType3, updatedProteinGoal,
                    updatedFibreGoal, updatedCalorieGoal, updatedCarbsGoal, updatedFatGoal, userId, fitnessId, weightId
                ).status
            )
        }


        @Test
        fun `updating a nutrition goal by goal id when it exists, returns 204 response`() {

            // Arrange - add a user and an associated nutrition goal that we plan to do an update on
            val fitnessId = 174
            val weightId = 304

            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addGoalResponse = addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )
            assertEquals(201, addGoalResponse.status)
            val addedGoal = jsonNodeToObject<NutritionGoal>(addGoalResponse)

            // Act and Assert - update the added nutrition goal and assert a 204 is returned
            val updatedGoalResponse = updateNutritionGoal(
                addedGoal.id, updatedType3, updatedProteinGoal,
                updatedFibreGoal, updatedCalorieGoal, updatedCarbsGoal, updatedFatGoal, addedUser.id, fitnessId, weightId
            )
            assertEquals(204, updatedGoalResponse.status)

            // After - delete the user
            testUtilities.deleteUser(addedUser.id)
        }

    }

    @Nested
    inner class DeleteNutritionGoals {

        @Test
        fun `deleting nutrition goals by user id when it doesn't exist, returns a 404 response`() {
            // Act and Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteNutritionGoalsByUserId(-1).status)
        }

        @Test
        fun `deleting all nutrition goals by user id when it exists, returns a 204 response`() {

            // Arrange - add a user and 3 associated nutrition goals that we plan to do a cascade delete
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val fitnessId = 174
            val weightId = 304

            val addGoalResponse1 = addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )
            assertEquals(201, addGoalResponse1.status)
            val addGoalResponse2 = addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )
            assertEquals(201, addGoalResponse2.status)
            val addGoalResponse3 = addNutritionGoal(
                nutritionGoals[0].type, nutritionGoals[0].proteinGoal,
                nutritionGoals[0].fibreGoal, nutritionGoals[0].calorieGoal, nutritionGoals[0].carbsGoal,
                nutritionGoals[0].fatGoal, addedUser.id, fitnessId, weightId
            )
            assertEquals(201, addGoalResponse3.status)

            // Act and Assert - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)

            // Act and Assert - attempt to retrieve the deleted nutrition goals
            val addedGoal1 = jsonNodeToObject<NutritionGoal>(addGoalResponse1)
            val addedGoal2 = jsonNodeToObject<NutritionGoal>(addGoalResponse2)
            val addedGoal3 = jsonNodeToObject<NutritionGoal>(addGoalResponse3)
            assertEquals(404, retrieveNutritionGoalByGoalId(addedGoal1.id).status)
            assertEquals(404, retrieveNutritionGoalByGoalId(addedGoal2.id).status)
            assertEquals(404, retrieveNutritionGoalByGoalId(addedGoal3.id).status)
        }
    }

    //--------------------------------------------------------------------------------------
// HELPER METHODS - could move them into a test utility class when submitting assignment
//--------------------------------------------------------------------------------------

    // Helper function to retrieve nutrition goals by weight goal id
    private fun retrieveNutritionGoalsByWeightGoalId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/weightGoals/$id/nutritionGoals").asJson()
    }

    // Helper function to retrieve nutrition goals by fitness goal id
    private fun retrieveNutritionGoalsByFitnessId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/fitnessGoals/$id/nutritionGoals").asJson()
    }

    // Helper function to retrieve all nutrition goals
    private fun retrieveAllNutritionGoals(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/nutritionGoals").asJson()
    }

    // Helper function to retrieve nutrition goals by user id
    private fun retrieveNutritionGoalsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/nutritionGoals").asJson()
    }

    // Helper function to retrieve a nutrition goal by goal id
    private fun retrieveNutritionGoalByGoalId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/nutritionGoals/${id}").asJson()
    }

    // Helper function to delete nutrition goals by user id
    private fun deleteNutritionGoalsByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/nutritionGoals/$id").asString()
    }

    // Helper function to add a nutrition goal
    private fun addNutritionGoal(
        type: String, proteinGoal: Double, fibreGoal: Double,
        calorieGoal: Double, carbsGoal: Double, fatGoal: Double, userId: Int, fitnessId: Int, weightId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/nutritionGoals")
            .body(
                """
    {
        "type": "$type",
        "proteinGoal": $proteinGoal,
        "fibreGoal": $fibreGoal,
        "calorieGoal": $calorieGoal,
        "carbsGoal": $carbsGoal,
        "fatGoal": $fatGoal,
        "userId": $userId,
        "fitnessId": $fitnessId,
        "weightId": $weightId
    }
    """.trimIndent()
            )
            .asJson()
    }

    // Helper function to update a nutrition goal
    private fun updateNutritionGoal(
        id: Int, type: String, proteinGoal: Double, fibreGoal: Double,
        calorieGoal: Double, carbsGoal: Double, fatGoal: Double, userId: Int, fitnessId: Int, weightId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/nutritionGoals/$id")
            .body(
                """
    {
        "type": "$type",
        "proteinGoal": $proteinGoal,
        "fibreGoal": $fibreGoal,
        "calorieGoal": $calorieGoal,
        "carbsGoal": $carbsGoal,
        "fatGoal": $fatGoal,
        "userId": $userId,
        "fitnessId": $fitnessId,
        "weightId": $weightId
    }
    """.trimIndent()
            )
            .asJson()
    }

}