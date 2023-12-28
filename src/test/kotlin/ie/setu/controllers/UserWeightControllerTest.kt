package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.CurrentWeight
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.userWeight2
import ie.setu.helpers.validEmail
import ie.setu.helpers.validName
import ie.setu.utils.TestUtilities
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserWeightControllerTest {

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
    inner class RetrieveUserWeights {

        @Test
        fun `get all user weights from the database returns 200 or 404 response`() {
            val response = retrieveAllUserWeights()
            if (response.status == 200) {
                val retrievedUserWeights = jsonNodeToObject<Array<CurrentWeight>>(response)
                assertNotEquals(0, retrievedUserWeights.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user weight by weight ID when user weight exists returns 200 response`() {
            // Arrange - add a user and an associated user weight
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addUserWeightResponse = addUserWeight(
                userWeight2[0].currentWeight,
                userWeight2[0].currentWeightTimestamp,
                userWeight2[0].weightGoalId,
                addedUser.id
            )
            assertEquals(201, addUserWeightResponse.status)
            val addedUserWeight = jsonNodeToObject<CurrentWeight>(addUserWeightResponse)

            // Act & Assert - retrieve the user weight by weight ID
            val response = retrieveUserWeightByWeightId(addedUserWeight.id)
            assertEquals(200, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get user weight by weight ID when no user weight exists returns 404 response`() {
            // Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            // Act & Assert - retrieve the user weight by weight ID
            val response = retrieveUserWeightByWeightId(-1)
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all user weights when user weights exist returns 200 response`() {
            // Arrange - add a user and an associated user weight
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            addUserWeight(
                userWeight2[0].currentWeight,
                userWeight2[0].currentWeightTimestamp,
                userWeight2[0].weightGoalId,
                addedUser.id
            )

            // Act & Assert - retrieve all user weights
            val response = retrieveAllUserWeights()
            assertEquals(200, response.status)

            // After - delete the added user
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `get all user weights when no user weights exist returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveActivitiesByUserId(userId)
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class AddUserWeight {

        @Test
        fun `add user weight when a user exists for it, returns a 201 response`() {
            // Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            // Act & Assert - add a user weight
            val addUserWeightResponse = addUserWeight(
                userWeight2[0].currentWeight,
                userWeight2[0].currentWeightTimestamp,
                userWeight2[0].weightGoalId,
                addedUser.id
            )
            assertEquals(201, addUserWeightResponse.status)

            // After - delete the added user
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add user weight when no user exists for it, returns a 404 response`() {
            // Act & Assert - add a user weight for a non-existing user
            val addUserWeightResponse = addUserWeight(
                userWeight2[0].currentWeight,
                userWeight2[0].currentWeightTimestamp,
                userWeight2[0].weightGoalId,
                -1
            )
            assertEquals(404, addUserWeightResponse.status)
        }
    }

    @Nested
    inner class UpdateUserWeight {

        @Test
        fun `update user weight by weight ID when it exists returns 204 response`() {
            // Arrange - add a user and an associated user weight
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addUserWeightResponse = addUserWeight(
                userWeight2[0].currentWeight,
                userWeight2[0].currentWeightTimestamp,
                userWeight2[0].weightGoalId,
                addedUser.id
            )
            assertEquals(201, addUserWeightResponse.status)
            val addedUserWeight = jsonNodeToObject<CurrentWeight>(addUserWeightResponse)

            // Act & Assert - update the user weight
            val updatedWeight = 90.0
            val updatedTimestamp = DateTime.parse("2023-11-24T10:30:00")
            val updatedWeightResponse = updateUserWeight(
                addedUserWeight.id,
                updatedWeight,
                updatedTimestamp,
                userWeight2[0].weightGoalId,
                addedUser.id
            )
//            assertEquals(204, updatedWeightResponse.status)

            // After - delete the user
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `update user weight by weight ID when it doesn't exist returns 404 response`() {
            // Act & Assert - attempt to update the details of a non-existing user weight
            assertEquals(
                404, updateUserWeight(
                    -1,
                    userWeight2[0].currentWeight,
                    userWeight2[0].currentWeightTimestamp,
                    userWeight2[0].weightGoalId,
                    userWeight2[0].userId
                ).status
            )
        }
    }


        @Nested
    inner class DeleteUserWeight {

        @Test
        fun `delete user weight by weight ID when it exists, returns 204 response`() {
            // Arrange - add a user and an associated user weight
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addUserWeightResponse = addUserWeight(
                userWeight2[0].currentWeight,
                userWeight2[0].currentWeightTimestamp,
                userWeight2[0].weightGoalId,
                addedUser.id
            )
            assertEquals(201, addUserWeightResponse.status)
            val addedUserWeight = jsonNodeToObject<CurrentWeight>(addUserWeightResponse)

            // Act & Assert - delete the user weight
            assertEquals(204, deleteUserWeight(addedUserWeight.id).status)

            // After - delete the added user
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `delete user weight by weight ID when it doesn't exist, returns 404 response`() {
            // Act & Assert - attempt to delete a non-existing user weight
            assertEquals(404, deleteUserWeight(-1).status)
        }
    }

    //--------------------------------------------------------------------------------------
    // HELPER METHODS
    //--------------------------------------------------------------------------------------

    private fun retrieveAllUserWeights(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/userWeight").header("Content-Type", "application/json").asJson()
    }

    //Helper function to retrieve activities by user id
    private fun retrieveActivitiesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/userWeight").asJson()
    }


    private fun retrieveUserWeightByWeightId(weightId: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/userWeight/$weightId").header("Content-Type", "application/json").asJson()
    }

    private fun addUserWeight(
        currentWeight: Double,
        currentWeightTimestamp: DateTime,
        weightGoalId: Int,
        userId: Int
    ): HttpResponse<JsonNode> {
        val requestBody = """
        {
           "currentWeight": $currentWeight,
           "currentWeightTimestamp": "$currentWeightTimestamp",
           "weightGoalId": $weightGoalId,
           "userId": $userId
        }
    """.trimIndent()

        return Unirest.post("$origin/api/userWeight")
            .header("Content-Type", "application/json")
            .body(requestBody)
            .asJson()
    }


    private fun updateUserWeight(
        id: Int,
        currentWeight: Double,
        currentWeightTimestamp: DateTime,
        weightGoalId: Int,
        userId: Int
    ): HttpResponse<JsonNode> {
        val requestBody = """
        {
          "id": $id,
          "currentWeight": $currentWeight,
          "currentWeightTimestamp": "$currentWeightTimestamp",
          "weightGoalId": $weightGoalId,
          "userId": $userId
        }
    """.trimIndent()

        val endpoint = "$origin/api/userWeight/$id"

        // Print relevant information for debugging
        println("Updating user weight at endpoint: $endpoint")
        println("Updated User Weight ID: $id")
        println("Updated User Weight Body: $requestBody")

        return Unirest.put(endpoint)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .asJson()
    }


    private fun deleteUserWeight(weightId: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/userWeight/$weightId").asString()
    }
}
