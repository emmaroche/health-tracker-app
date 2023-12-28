package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.*
import ie.setu.utils.TestUtilities
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityControllerTest {

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
    inner class ReadActivities {

        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = retrieveAllActivities()
            if (response.status == 200) {
                val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
                assertNotEquals(0, retrievedActivities.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all activities by user id when user and activities exist returns 200 response`() {
            // Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

            // Replace 1 with a valid fitness goal ID
            val fitnessId = 457

            addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id, fitnessId
            )
            addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id, fitnessId
            )
            addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id, fitnessId
            )

            // Act - retrieve the three added activities by user id
            val response = retrieveActivitiesByUserId(addedUser.id)

            // Assert
            assertEquals(200, response.status)
            val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
            assertEquals(3, retrievedActivities.size)

            // After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }


        @Test
        fun `get all activities by user id when no activities exist returns 404 response`() {
            //Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveActivitiesByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveActivitiesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get activity by activity id when no activity exists returns 404 response`() {
            //Arrange
            val activityId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = retrieveActivityByActivityId(activityId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get activity by activity id when activity exists returns 200 response`() {
            // Arrange - add a user and associated activity
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

            // Replace 1 with a valid fitness goal ID
            val fitnessId = 457

            val addActivityResponse = addActivity(
                activities[0].description,
                activities[0].duration, activities[0].calories,
                activities[0].started, addedUser.id, fitnessId
            )
            assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

            // Act & Assert - retrieve the activity by activity id
            val response = retrieveActivityByActivityId(addedActivity.id)
            assertEquals(200, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get activities by fitness ID when fitness goal exists and has activities returns 200 response`() {
            // Arrange - add a user and an associated fitness goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())
            val fitnessGoalId = 457

            // Add three associated activities that we plan to retrieve
            addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id, fitnessGoalId
            )
            addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id, fitnessGoalId
            )
            addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id, fitnessGoalId
            )

            // Act - retrieve the three added activities by fitness goal id
            val response = retrieveActivitiesByFitnessId(fitnessGoalId)

            // Assert
            assertEquals(200, response.status)
            val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
            assertEquals(4, retrievedActivities.size)

            // After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get activities by fitness ID when fitness goal does not exist returns 404 response`() {
            // Arrange - add a user without adding a fitness goal
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

            // Act - retrieve activities by fitness goal id
            val response = retrieveActivitiesByFitnessId(-1) // Replace -1 with a valid fitness goal ID

            // Assert
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Nested
        inner class CreateActivities {

            @Test
            fun `add an activity when a user exists for it, returns a 201 response`() {

                // Arrange - add a user and an associated activity that we plan to delete
                val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

                // Replace 1 with a valid fitness goal ID
                val fitnessId = 457

                val addActivityResponse = addActivity(
                    activities[0].description,
                    activities[0].duration, activities[0].calories,
                    activities[0].started, addedUser.id, fitnessId
                )
                assertEquals(201, addActivityResponse.status)

                // After - delete the user (Activity will cascade delete in the database)
                testUtilities.deleteUser(addedUser.id)
            }


            @Test
            fun `add an activity when no user exists for it, returns a 404 response`() {

                // Arrange - check there is no user for -1 id
                val userId = -1
                assertEquals(404, testUtilities.retrieveUserById(userId).status)

                // Replace 1 with a valid fitness goal ID
                val fitnessId = 457

                val addActivityResponse = addActivity(
                    activities[0].description,
                    activities[0].duration, activities[0].calories,
                    activities[0].started, userId, fitnessId
                )
                assertEquals(404, addActivityResponse.status)
            }

        }

        @Nested
        inner class UpdateActivities {

            @Test
            fun `updating an activity by activity id when it doesn't exist, returns a 404 response`() {
                val userId = -1
                val activityID = -1

                // Arrange - check there is no user for -1 id
                assertEquals(404, testUtilities.retrieveUserById(userId).status)

                // Act & Assert - attempt to update the details of an activity/user that doesn't exist
                assertEquals(
                    404, updateActivity(
                        activityID, updatedDescription, updatedDuration,
                        updatedCalories, updatedStarted, userId, 1 // Replace with a valid fitness goal ID
                    ).status
                )
            }

            @Test
            fun `updating an activity by activity id when it exists, returns 204 response`() {

                // Arrange - add a user and an associated activity that we plan to do an update on
                val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

                // Replace 1 with a valid fitness goal ID
                val fitnessId = 457

                val addActivityResponse = addActivity(
                    activities[0].description,
                    activities[0].duration, activities[0].calories,
                    activities[0].started, addedUser.id, fitnessId
                )
                assertEquals(201, addActivityResponse.status)
                val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

                // Act & Assert - update the added activity and assert a 204 is returned
                val updatedActivityResponse = updateActivity(
                    addedActivity.id, updatedDescription,
                    updatedDuration, updatedCalories, updatedStarted, addedUser.id, fitnessId
                )
                assertEquals(204, updatedActivityResponse.status)

                // Assert that the individual fields were all updated as expected
                val retrievedActivityResponse = retrieveActivityByActivityId(addedActivity.id)
                val updatedActivity = jsonNodeToObject<Activity>(retrievedActivityResponse)
                assertEquals(updatedDescription, updatedActivity.description)
                assertEquals(updatedDuration, updatedActivity.duration, 0.1)
                assertEquals(updatedCalories, updatedActivity.calories)
                assertEquals(updatedStarted, updatedActivity.started)

                // After - delete the user
                testUtilities.deleteUser(addedUser.id)
            }

        }

        @Nested
        inner class DeleteActivities {

            @Test
            fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
                //Act & Assert - attempt to delete a user that doesn't exist
                assertEquals(404, deleteActivityByActivityId(-1).status)
            }

            @Test
            fun `deleting activities by user id when it doesn't exist, returns a 404 response`() {
                //Act & Assert - attempt to delete a user that doesn't exist
                assertEquals(404, deleteActivitiesByUserId(-1).status)
            }

            @Test
            fun `deleting an activity by id when it exists, returns a 204 response`() {

                // Arrange - add a user and an associated activity that we plan to do a delete on
                val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

                // Replace 1 with a valid fitness goal ID
                val fitnessId = 457

                val addActivityResponse = addActivity(
                    activities[0].description,
                    activities[0].duration, activities[0].calories,
                    activities[0].started, addedUser.id, fitnessId
                )
                assertEquals(201, addActivityResponse.status)

                // Act & Assert - delete the added activity and assert a 204 is returned
                val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)
                assertEquals(204, deleteActivityByActivityId(addedActivity.id).status)

                // After - delete the user
                testUtilities.deleteUser(addedUser.id)
            }


            @Test
            fun `deleting all activities by userid when it exists, returns a 204 response`() {

                // Arrange - add a user and 3 associated activities that we plan to do a cascade delete
                val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

                // Replace 1 with a valid fitness goal ID
                val fitnessId = 457

                val addActivityResponse1 = addActivity(
                    activities[0].description,
                    activities[0].duration, activities[0].calories,
                    activities[0].started, addedUser.id, fitnessId
                )
                assertEquals(201, addActivityResponse1.status)

                val addActivityResponse2 = addActivity(
                    activities[1].description,
                    activities[1].duration, activities[1].calories,
                    activities[1].started, addedUser.id, fitnessId
                )
                assertEquals(201, addActivityResponse2.status)

                val addActivityResponse3 = addActivity(
                    activities[2].description,
                    activities[2].duration, activities[2].calories,
                    activities[2].started, addedUser.id, fitnessId
                )
                assertEquals(201, addActivityResponse3.status)

                // Act & Assert - delete the added user and assert a 204 is returned
                assertEquals(204, testUtilities.deleteUser(addedUser.id).status)

                // Act & Assert - attempt to retrieve the deleted activities
                val addedActivity1 = jsonNodeToObject<Activity>(addActivityResponse1)
                val addedActivity2 = jsonNodeToObject<Activity>(addActivityResponse2)
                val addedActivity3 = jsonNodeToObject<Activity>(addActivityResponse3)
                assertEquals(404, retrieveActivityByActivityId(addedActivity1.id).status)
                assertEquals(404, retrieveActivityByActivityId(addedActivity2.id).status)
                assertEquals(404, retrieveActivityByActivityId(addedActivity3.id).status)
            }

        }

        //--------------------------------------------------------------------------------------
        // HELPER METHODS - could move them into a test utility class when submitting assignment
        //--------------------------------------------------------------------------------------

        //Helper function to retrieve all activities
        private fun retrieveAllActivities(): HttpResponse<JsonNode> {
            return Unirest.get("$origin/api/activities").asJson()
        }

        // Helper function to retrieve activities by fitness goal id
        private fun retrieveActivitiesByFitnessId(id: Int): HttpResponse<JsonNode> {
            return Unirest.get("$origin/api/fitnessGoals/$id/activities").asJson()
        }

        //Helper function to retrieve activities by user id
        private fun retrieveActivitiesByUserId(id: Int): HttpResponse<JsonNode> {
            return Unirest.get(origin + "/api/users/${id}/activities").asJson()
        }

        //Helper function to retrieve activity by activity id
        private fun retrieveActivityByActivityId(id: Int): HttpResponse<JsonNode> {
            return Unirest.get(origin + "/api/activities/${id}").asJson()
        }

        //Helper function to delete an activity by activity id
        private fun deleteActivityByActivityId(id: Int): HttpResponse<String> {
            return Unirest.delete("$origin/api/activities/$id").asString()
        }

        //Helper function to delete an activity by activity id
        private fun deleteActivitiesByUserId(id: Int): HttpResponse<String> {
            return Unirest.delete("$origin/api/users/$id/activities").asString()
        }

        // Helper function to update an activity
        private fun updateActivity(
            id: Int, description: String, duration: Double, calories: Int,
            started: DateTime, userId: Int, fitnessId: Int
        ): HttpResponse<JsonNode> {
            return Unirest.patch("$origin/api/activities/$id")
                .body(
                    """
            {
              "description":"$description",
              "duration":$duration,
              "calories":$calories,
              "started":"$started",
              "userId":$userId,
              "fitnessId":$fitnessId
            }
        """.trimIndent()
                ).asJson()
        }

        // Helper function to add an activity
        private fun addActivity(
            description: String, duration: Double, calories: Int,
            started: DateTime, userId: Int, fitnessId: Int
        ): HttpResponse<JsonNode> {
            return Unirest.post("$origin/api/activities")
                .body(
                    """
            {
               "description":"$description",
               "duration":$duration,
               "calories":$calories,
               "started":"$started",
               "userId":$userId,
               "fitnessId":$fitnessId
            }
        """.trimIndent()
                )
                .asJson()
        }
    }
}