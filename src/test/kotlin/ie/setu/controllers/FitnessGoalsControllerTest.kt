package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.FitnessGoal
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
class FitnessGoalsControllerTest {

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
    inner class ReadFitnessGoals {

        @Test
        fun `get all fitness goals from the database returns 200 or 404 response`() {
            val response = retrieveAllFitnessGoals()
            if (response.status == 200) {
                val retrievedFitnessGoals = jsonNodeToObject<Array<FitnessGoal>>(response)
                assertNotEquals(0, retrievedFitnessGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all fitness goals by user id when user and fitness goals exist returns 200 response`() {
            // Arrange - add a user and 3 associated fitness goals that we plan to retrieve
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].workoutsPerWeek, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, addedUser.id
            )
            addFitnessGoal(
                fitnessGoals[1].type, fitnessGoals[1].minutesOfWorkouts, fitnessGoals[1].minutesOfWorkouts,
                fitnessGoals[1].calorieBurningGoalDuringExercise, addedUser.id
            )
            addFitnessGoal(
                fitnessGoals[2].type, fitnessGoals[2].minutesOfWorkouts, fitnessGoals[2].minutesOfWorkouts,
                fitnessGoals[2].calorieBurningGoalDuringExercise, addedUser.id
            )

            // Act and Assert - retrieve the three added fitness goals by user id
            val response = retrieveFitnessGoalsByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedFitnessGoals = jsonNodeToObject<Array<FitnessGoal>>(response)
            assertEquals(3, retrievedFitnessGoals.size)

            // After - delete the added user and assert a 204 is returned (fitness goals are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all fitness goals by user id when no fitness goals exist returns 404 response`() {
            // Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            // Act and Assert - retrieve the fitness goals by user id
            val response = retrieveFitnessGoalsByUserId(addedUser.id)
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all fitness goals by user id when no user exists returns 404 response`() {
            // Arrange
            val userId = -1

            // Act and Assert - retrieve fitness goals by user id
            val response = retrieveFitnessGoalsByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get fitness goal by goal id when no fitness goal exists returns 404 response`() {
            // Arrange
            val goalId = -1

            // Act and Assert - attempt to retrieve the fitness goal by goal id
            val response = retrieveFitnessGoalByGoalId(goalId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get fitness goal by goal id when fitness goal exists returns 200 response`() {
            // Arrange - add a user and an associated fitness goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addGoalResponse = addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].workoutsPerWeek, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, addedUser.id
            )
            assertEquals(201, addGoalResponse.status)
            val addedGoal = jsonNodeToObject<FitnessGoal>(addGoalResponse)

            // Act and Assert - retrieve the fitness goal by goal id
            val response = retrieveFitnessGoalByGoalId(addedGoal.id)
            assertEquals(200, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class CreateFitnessGoals {

        @Test
        fun `add a fitness goal when a user exists for it, returns a 201 response`() {

            // Arrange - add a user and an associated fitness goal that we plan to do a delete on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            val addGoalResponse = addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].workoutsPerWeek, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, addedUser.id
            )
            assertEquals(201, addGoalResponse.status)

            // After - delete the user (fitness goal will cascade delete in the database)
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add a fitness goal when no user exists for it, returns a 404 response`() {

            // Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            val addGoalResponse = addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].workoutsPerWeek, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, userId
            )

            assertEquals(404, addGoalResponse.status)
        }
    }

    @Nested
    inner class UpdateFitnessGoals {

        @Test
        fun `updating a fitness goal by goal id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val goalId = -1

            // Arrange - check there is no user for -1 id
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            // Act and Assert - attempt to update the details of a fitness goal/user that doesn't exist
            assertEquals(
                404, updateFitnessGoal(
                    goalId, updatedType, updatedWorkoutsPerWeek, updatedMinutesOfWorkouts,
                    updatedCalorieBurningGoalDuringExercise, userId
                ).status
            )
        }

        @Test
        fun `updating a fitness goal by goal id when it exists, returns 204 response`() {

            // Arrange - add a user and an associated fitness goal that we plan to do an update on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addGoalResponse = addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].workoutsPerWeek, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, addedUser.id
            )
            assertEquals(201, addGoalResponse.status)
            val addedGoal = jsonNodeToObject<FitnessGoal>(addGoalResponse)

            // Act and Assert - update the added fitness goal and assert a 204 is returned
            val updatedGoalResponse = updateFitnessGoal(
                addedGoal.id, updatedType, updatedWorkoutsPerWeek, updatedMinutesOfWorkouts,
                updatedCalorieBurningGoalDuringExercise, addedUser.id
            )
            assertEquals(204, updatedGoalResponse.status)

            // After - delete the user
            testUtilities.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteFitnessGoals {

        @Test
        fun `deleting fitness goals by user id when it doesn't exist, returns a 404 response`() {
            // Act and Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteFitnessGoalsByUserId(-1).status)
        }

        @Test
        fun `deleting all fitness goals by user id when it exists, returns a 204 response`() {

            // Arrange - add a user and 3 associated fitness goals that we plan to do a cascade delete
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addGoalResponse1 = addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].workoutsPerWeek, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, addedUser.id
            )
            assertEquals(201, addGoalResponse1.status)
            val addGoalResponse2 = addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].minutesOfWorkouts, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, addedUser.id
            )
            assertEquals(201, addGoalResponse2.status)
            val addGoalResponse3 = addFitnessGoal(
                fitnessGoals[0].type, fitnessGoals[0].minutesOfWorkouts, fitnessGoals[0].minutesOfWorkouts,
                fitnessGoals[0].calorieBurningGoalDuringExercise, addedUser.id
            )
            assertEquals(201, addGoalResponse3.status)

            // Act and Assert - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)

            // Act and Assert - attempt to retrieve the deleted fitness goals
            val addedGoal1 = jsonNodeToObject<FitnessGoal>(addGoalResponse1)
            val addedGoal2 = jsonNodeToObject<FitnessGoal>(addGoalResponse2)
            val addedGoal3 = jsonNodeToObject<FitnessGoal>(addGoalResponse3)
            assertEquals(404, retrieveFitnessGoalByGoalId(addedGoal1.id).status)
            assertEquals(404, retrieveFitnessGoalByGoalId(addedGoal2.id).status)
            assertEquals(404, retrieveFitnessGoalByGoalId(addedGoal3.id).status)
        }
    }

    //--------------------------------------------------------------------------------------
    // HELPER METHODS - could move them into a test utility class when submitting assignment
    //--------------------------------------------------------------------------------------

    // Helper function to retrieve all fitness goals
    private fun retrieveAllFitnessGoals(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/fitnessGoals").asJson()
    }

    // Helper function to retrieve fitness goals by user id
    private fun retrieveFitnessGoalsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/fitnessGoals").asJson()
    }

    // Helper function to retrieve a fitness goal by goal id
    private fun retrieveFitnessGoalByGoalId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/fitnessGoals/${id}").asJson()
    }

    // Helper function to delete fitness goals by user id
    private fun deleteFitnessGoalsByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/fitnessGoals/$id").asString()
    }

    // Helper function to update a fitness goal
    private fun updateFitnessGoal(
        id: Int, type: String, workoutsPerWeek: Int, minutesOfWorkouts: Int, calorieBurningGoal: Double, userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/fitnessGoals/$id")
            .body(
                """
            {
                "type": "$type",
                "workoutsPerWeek": $workoutsPerWeek,
                "minutesOfWorkouts": $minutesOfWorkouts,
                "calorieBurningGoalDuringExercise": $calorieBurningGoal,
                "userId": $userId
            }
            """.trimIndent()
            )
            .asJson()
    }

    // Helper function to add a fitness goal
    private fun addFitnessGoal(
        type: String, workoutsPerWeek: Int, minutesOfWorkouts: Int, calorieBurningGoal: Double, userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/fitnessGoals")
            .body(
                """
            {
                "type": "$type",
                "workoutsPerWeek": $workoutsPerWeek,
                "minutesOfWorkouts": $minutesOfWorkouts,
                "calorieBurningGoalDuringExercise": $calorieBurningGoal,
                "userId": $userId
            }
            """.trimIndent()
            )
            .asJson()
    }


}
