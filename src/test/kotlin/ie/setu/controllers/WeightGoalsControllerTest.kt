package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.User
import ie.setu.domain.WeightGoal
import ie.setu.helpers.*
import ie.setu.utils.TestUtilities
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeightGoalsControllerTest {

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
    inner class ReadWeightGoals {

        @Test
        fun `get all weight goals from the database returns 200 or 404 response`() {
            val response = retrieveAllWeightGoals()
            if (response.status == 200) {
                val retrievedWeightGoals = jsonNodeToObject<Array<WeightGoal>>(response)
                assertNotEquals(0, retrievedWeightGoals.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all weight goals by user id when user and weight goals exist returns 200 response`() {
            // Arrange - add a user and 3 associated weight goals that we plan to retrieve
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
            )
            addWeightGoal(
                weightGoals[1].type, weightGoals[1].startingWeight, weightGoals[1].startingWeightTimestamp,
                weightGoals[1].targetWeight,
                weightGoals[1].weeklyGoal, weightGoals[1].deadline, addedUser.id
            )
            addWeightGoal(
                weightGoals[2].type, weightGoals[2].startingWeight, weightGoals[2].startingWeightTimestamp,
                weightGoals[2].targetWeight,
                weightGoals[2].weeklyGoal, weightGoals[2].deadline, addedUser.id
            )

            // Act and Assert - retrieve the three added weight goals by user id
            val response = retrieveWeightGoalsByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedWeightGoals = jsonNodeToObject<Array<WeightGoal>>(response)
            assertEquals(3, retrievedWeightGoals.size)

            // After - delete the added user and assert a 204 is returned (weight goals are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all weight goals by user id when no weight goals exist returns 404 response`() {
            // Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            // Act and Assert - retrieve the weight goals by user id
            val response = retrieveWeightGoalsByUserId(addedUser.id)
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all weight goals by user id when no user exists returns 404 response`() {
            // Arrange
            val userId = -1

            // Act and Assert - retrieve weight goals by user id
            val response = retrieveWeightGoalsByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get weight goal by goal id when no weight goal exists returns 404 response`() {
            // Arrange
            val goalId = -1

            // Act and Assert - attempt to retrieve the weight goal by goal id
            val response = retrieveWeightGoalByGoalId(goalId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get weight goal by goal id when weight goal exists returns 200 response`() {
            // Arrange - add a user and an associated weight goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addGoalResponse = addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
            )
            assertEquals(201, addGoalResponse.status)
            val addedGoal = jsonNodeToObject<WeightGoal>(addGoalResponse)

            // Act and Assert - retrieve the weight goal by goal id
            val response = retrieveWeightGoalByGoalId(addedGoal.id)
            assertEquals(200, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class CreateWeightGoals {

        @Test
        fun `add a weight goal when a user exists for it, returns a 201 response`() {

            // Arrange - add a user and an associated weight goal that we plan to do a delete on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            val addGoalResponse = addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
            )
            assertEquals(201, addGoalResponse.status)

            // After - delete the user (weight goal will cascade delete in the database)
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add a weight goal when no user exists for it, returns a 404 response`() {

            // Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            val addGoalResponse = addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, userId
            )

            assertEquals(404, addGoalResponse.status)
        }
    }

    @Nested
    inner class UpdateWeightGoals {

        @Test
        fun `updating a weight goal by goal id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val goalId = -1

            // Arrange - check there is no user for -1 id
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            // Act and Assert - attempt to update the details of a weight goal/user that doesn't exist
            assertEquals(
                404, updateWeightGoal(
                    goalId, updatedType2, updatedStartingWeight, updatedStartingWeightTimestamp,
                    updatedTargetWeight, updatedWeeklyGoal, updatedDeadline, userId
                ).status
            )
        }

        @Test
        fun `updating a weight goal by goal id when it exists, returns 204 response`() {
            // Arrange - add a user and an associated weight goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addGoalResponse = addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
            )
            assertEquals(201, addGoalResponse.status)
            val addedGoal = jsonNodeToObject<WeightGoal>(addGoalResponse)

            // Act & Assert - update the added goal and assert a 204 is returned
            val updatedGoalResponse = updateWeightGoal(
                addedGoal.id, updatedType2, updatedStartingWeight, updatedStartingWeightTimestamp,
                updatedTargetWeight, updatedWeeklyGoal, updatedDeadline, addedUser.id
            )

            if (updatedGoalResponse.body != null) {
                // Now you can safely access the response body
                println(updatedGoalResponse.body.toString())
            }

            assertEquals(204, updatedGoalResponse.status)

            // After - delete the user
            testUtilities.deleteUser(addedUser.id)
        }

//        @Test
//        fun `updating a weight goal by goal id when it exists, returns 204 response`() {
//            // Arrange - add a user and an associated weight goal
//            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
//            val addGoalResponse = addWeightGoal(
//                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
//                weightGoals[0].currentWeight, weightGoals[0].targetWeight,
//                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
//            )
//            assertEquals(201, addGoalResponse.status)
//            val addedGoal = jsonNodeToObject<WeightGoal>(addGoalResponse)
//
//            //Act & Assert - update the added goal and assert a 204 is returned
//            val updatedGoalResponse = updateWeightGoal(
//                addedGoal.id, updatedType2, updatedStartingWeight, updatedStartingWeightTimestamp,
//                updatedCurrentWeight, updatedTargetWeight, updatedWeeklyGoal, updatedDeadline, addedUser.id)
//            assertEquals(204, updatedGoalResponse.status)
//
//            //Assert that the individual fields were all updated as expected
//            val retrievedGoalResponse = retrieveWeightGoalByGoalId(addedGoal.id)
//            val updatedGoals = jsonNodeToObject<WeightGoal>(retrievedGoalResponse)
//            assertEquals(updatedDescription, updatedGoals.type)
//            assertEquals(updatedDuration, updatedGoals.startingWeight)
//            assertEquals(updatedCalories, updatedGoals.startingWeightTimestamp)
//            assertEquals(updatedStarted, updatedGoals.currentWeight )
//            assertEquals(updatedStarted, updatedGoals.targetWeight )
//            assertEquals(updatedStarted, updatedGoals.weeklyGoal )
//            assertEquals(updatedStarted, updatedGoals.deadline )
//
//
////            //After - delete the user
//            testUtilities.deleteUser(addedUser.id)
//        }

    }

    @Nested
    inner class DeleteWeightGoals {

        @Test
        fun `deleting weight goals by user id when it doesn't exist, returns a 404 response`() {
            // Act and Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteWeightGoalsByUserId(-1).status)
        }

        @Test
        fun `deleting all weight goals by user id when it exists, returns a 204 response`() {

            // Arrange - add a user and 3 associated weight goals that we plan to do a cascade delete
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addGoalResponse1 = addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
            )
            assertEquals(201, addGoalResponse1.status)
            val addGoalResponse2 = addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
            )
            assertEquals(201, addGoalResponse2.status)
            val addGoalResponse3 = addWeightGoal(
                weightGoals[0].type, weightGoals[0].startingWeight, weightGoals[0].startingWeightTimestamp,
                weightGoals[0].targetWeight,
                weightGoals[0].weeklyGoal, weightGoals[0].deadline, addedUser.id
            )
            assertEquals(201, addGoalResponse3.status)

            // Act and Assert - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)

            // Act and Assert - attempt to retrieve the deleted weight goals
            val addedGoal1 = jsonNodeToObject<WeightGoal>(addGoalResponse1)
            val addedGoal2 = jsonNodeToObject<WeightGoal>(addGoalResponse2)
            val addedGoal3 = jsonNodeToObject<WeightGoal>(addGoalResponse3)
            assertEquals(404, retrieveWeightGoalByGoalId(addedGoal1.id).status)
            assertEquals(404, retrieveWeightGoalByGoalId(addedGoal2.id).status)
            assertEquals(404, retrieveWeightGoalByGoalId(addedGoal3.id).status)
        }
    }

    //--------------------------------------------------------------------------------------
    // HELPER METHODS
    //--------------------------------------------------------------------------------------

    // Helper function to retrieve all weight goals
    private fun retrieveAllWeightGoals(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/weightGoals").asJson()
    }

    // Helper function to retrieve weight goals by user id
    private fun retrieveWeightGoalsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/weightGoals").asJson()
    }

    // Helper function to retrieve a weight goal by goal id
    private fun retrieveWeightGoalByGoalId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/weightGoals/${id}").asJson()
    }

    // Helper function to delete weight goals by user id
    private fun deleteWeightGoalsByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/weightGoals/$id").asString()
    }

    // Helper function to update a weight goal
    private fun updateWeightGoal(
        id: Int, type: String, startingWeight: Double, startingWeightTimestamp: DateTime,
        targetWeight: Double, weeklyGoal: Double, deadline: DateTime, userId: Int
    ): HttpResponse<JsonNode> {
        val startingWeightTimestampStr =
            startingWeightTimestamp.toString(ISODateTimeFormat.dateTime())  // Format as ISO 8601
        val deadlineStr = deadline.toString(ISODateTimeFormat.dateTime())  // Format as ISO 8601

        return Unirest.patch(origin + "/api/weightGoals/$id")
            .body(
                """
    {
        "type": "$type",
        "startingWeight": $startingWeight,
        "startingWeightTimestamp": "$startingWeightTimestampStr",
        "targetWeight": $targetWeight,
        "weeklyGoal": $weeklyGoal,
        "deadline": "$deadlineStr",
        "userId": $userId
    }
        """.trimIndent()
            )
            .asJson()
    }

    // Helper function to add a weight goal
    private fun addWeightGoal(
        type: String,
        startingWeight: Double,
        startingWeightTimestamp: DateTime,
        targetWeight: Double,
        weeklyGoal: Double,
        deadline: DateTime,
        userId: Int
    ): HttpResponse<JsonNode> {
        //ChatGPT generated lines 338 and 339 to help with a formatting error
        val startingWeightTimestampStr =
            startingWeightTimestamp.toString(ISODateTimeFormat.dateTime())  // Format as ISO 8601
        val deadlineStr = deadline.toString(ISODateTimeFormat.dateTime())  // Format as ISO 8601

        return Unirest.post(origin + "/api/weightGoals")
            .body(
                """
        {
            "type": "$type",
            "startingWeight": $startingWeight,
            "startingWeightTimestamp": "$startingWeightTimestampStr",
            "targetWeight": $targetWeight,
            "weeklyGoal": $weeklyGoal,
            "deadline": "$deadlineStr",
            "userId": $userId
        }
        """.trimIndent()
            )
            .asJson()
    }
}
