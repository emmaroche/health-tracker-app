package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.MoodEntry
import ie.setu.domain.User
import ie.setu.helpers.*
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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MoodTrackingControllerTest {

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
    inner class ReadMoodEntries {

        @Test
        fun `get all mood entries from the database returns 200 or 404 response`() {
            val response = retrieveAllMoodEntries()
            if (response.status == 200) {
                val retrievedEntries = jsonNodeToObject<Array<MoodEntry>>(response)
                assertNotEquals(0, retrievedEntries.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all mood entries by user id when user and mood entries exist returns 200 response`() {
            // Arrange - add a user and 3 associated mood entries that we plan to retrieve
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val sleepId = 14

            addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            addMoodEntry(
                moodTracking[1].date, moodTracking[1].mood, moodTracking[1].rating,
                moodTracking[1].notes, addedUser.id, sleepId
            )
            addMoodEntry(
                moodTracking[2].date, moodTracking[2].mood, moodTracking[2].rating,
                moodTracking[2].notes, addedUser.id, sleepId
            )

            // Act & Assert - retrieve the three added mood entries by user id
            val response = retrieveMoodEntriesByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedEntries = jsonNodeToObject<Array<MoodEntry>>(response)
            assertEquals(3, retrievedEntries.size)

            // After - delete the added user and assert a 204 is returned (mood entries are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all mood entries by user id when no mood entries exist returns 404 response`() {
            // Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            // Act & Assert - retrieve the mood entries by user id
            val response = retrieveMoodEntriesByUserId(addedUser.id)
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all mood entries by user id when no user exists returns 404 response`() {
            // Arrange
            val userId = -1

            // Act & Assert - retrieve mood entries by user id
            val response = retrieveMoodEntriesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get mood entry by mood entry id when no mood entry exists returns 404 response`() {
            // Arrange
            val entryId = -1

            // Act & Assert - attempt to retrieve the mood entry by mood entry id
            val response = retrieveMoodEntryByMoodEntryId(entryId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get mood entry by mood entry id when mood entry exists returns 200 response`() {
            // Arrange - add a user and associated mood entry
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val sleepId = 14
            val addEntryResponse = addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            assertEquals(201, addEntryResponse.status)
            val addedEntry = jsonNodeToObject<MoodEntry>(addEntryResponse)

            // Act & Assert - retrieve the mood entry by mood entry id
            val response = retrieveMoodEntryByMoodEntryId(addedEntry.id)
            assertEquals(200, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get mood entries by sleep tracking id when sleep tracking id exists and has mood entries returns 200 response`() {
            // Arrange - add a sleep entry and associated mood entries
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val sleepId = 14

            // Add three associated mood entries
            addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            addMoodEntry(
                moodTracking[1].date, moodTracking[1].mood, moodTracking[1].rating,
                moodTracking[1].notes, addedUser.id, sleepId
            )
            addMoodEntry(
                moodTracking[2].date, moodTracking[2].mood, moodTracking[2].rating,
                moodTracking[2].notes, addedUser.id, sleepId
            )

            // Act - retrieve the three added mood entries by sleep tracking id
            val response = retrieveMoodEntriesBySleepTrackingId(sleepId)

            // Assert
            assertEquals(200, response.status)
            val retrievedEntries = jsonNodeToObject<Array<MoodEntry>>(response)
            assertEquals(4, retrievedEntries.size)

            // After - delete the added user and assert a 204 is returned (mood entries are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get mood entries by sleep tracking id when sleep tracking id exists and has no mood entries returns 404 response`() {
            // Arrange - add a sleep entry
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val sleepTrackingId = 456

            // Add a sleep entry with no associated mood entries

            // Act - retrieve the mood entries by sleep tracking id
            val response = retrieveMoodEntriesBySleepTrackingId(sleepTrackingId)

            // Assert
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get mood entries by sleep tracking id when sleep tracking id does not exist returns 404 response`() {
            // Arrange
            val sleepTrackingId = -1

            // Act - retrieve mood entries by sleep tracking id
            val response = retrieveMoodEntriesBySleepTrackingId(sleepTrackingId)

            // Assert
            assertEquals(404, response.status)
        }

    }

    @Nested
    inner class CreateMoodEntries {

        @Test
        fun `add a mood entry when a user exists for it, returns a 201 response`() {

            // Arrange - add a user and an associated mood entry that we plan to do a delete on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val sleepId = 14

            val addEntryResponse = addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            assertEquals(201, addEntryResponse.status)

            // After - delete the user (mood entry will cascade delete in the database)
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add a mood entry when no user exists for it, returns a 404 response`() {

            // Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, testUtilities.retrieveUserById(userId).status)
            val sleepId = 14

            val addEntryResponse = addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, userId, sleepId
            )
            assertEquals(404, addEntryResponse.status)
        }
    }

    @Nested
    inner class UpdateMoodEntries {

        @Test
        fun `updating a mood entry by mood entry id when it doesn't exist, returns a 404 response`() {


            val userId = -1
            val entryId = -1
            val sleepId = 14

            // Arrange - check there is no user for -1 id
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            // Act & Assert - attempt to update the details of a mood entry/user that doesn't exist
            assertEquals(
                404, updateMoodEntry(
                    entryId, updatedDate, updatedMood, updatedRating,
                    updatedNotes, userId, sleepId
                ).status
            )
        }

        @Test
        fun `updating a mood entry by mood entry id when it exists, returns 204 response`() {

            // Arrange - add a user and an associated mood entry that we plan to do an update on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val sleepId = 14
            val addEntryResponse = addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            assertEquals(201, addEntryResponse.status)
            val addedEntry = jsonNodeToObject<MoodEntry>(addEntryResponse)

            // Act & Assert - update the added mood entry and assert a 204 is returned
            val updatedEntryResponse = updateMoodEntry(
                addedEntry.id, updatedDate, updatedMood, updatedRating,
                updatedNotes, addedUser.id, sleepId
            )
            assertEquals(204, updatedEntryResponse.status)

            // Assert that the individual fields were all updated as expected
            val retrievedEntryResponse = retrieveMoodEntryByMoodEntryId(addedEntry.id)
            val updatedEntry = jsonNodeToObject<MoodEntry>(retrievedEntryResponse)
            assertEquals(updatedDate, updatedEntry.date)
            assertEquals(updatedMood, updatedEntry.mood)
            assertEquals(updatedRating, updatedEntry.rating)
            assertEquals(updatedNotes, updatedEntry.notes)

            // After - delete the user
            testUtilities.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteMoodEntries {

        @Test
        fun `deleting mood entries by user id when it doesn't exist, returns a 404 response`() {
            // Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteMoodEntryByMoodEntryId(-1).status)
        }

        @Test
        fun `deleting all mood entries by user id when it exists, returns a 204 response`() {

            // Arrange - add a user and 3 associated mood entries that we plan to do a cascade delete
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val sleepId = 14
            val addEntryResponse1 = addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            assertEquals(201, addEntryResponse1.status)
            val addEntryResponse2 = addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            assertEquals(201, addEntryResponse2.status)
            val addEntryResponse3 = addMoodEntry(
                moodTracking[0].date, moodTracking[0].mood, moodTracking[0].rating,
                moodTracking[0].notes, addedUser.id, sleepId
            )
            assertEquals(201, addEntryResponse3.status)

            // Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)

            // Act & Assert - attempt to retrieve the deleted mood entries
            val addedEntry1 = jsonNodeToObject<MoodEntry>(addEntryResponse1)
            val addedEntry2 = jsonNodeToObject<MoodEntry>(addEntryResponse2)
            val addedEntry3 = jsonNodeToObject<MoodEntry>(addEntryResponse3)
            assertEquals(404, retrieveMoodEntryByMoodEntryId(addedEntry1.id).status)
            assertEquals(404, retrieveMoodEntryByMoodEntryId(addedEntry2.id).status)
            assertEquals(404, retrieveMoodEntryByMoodEntryId(addedEntry3.id).status)
        }
    }

    //--------------------------------------------------------------------------------------
    // HELPER METHODS
    //--------------------------------------------------------------------------------------

    // Helper function to retrieve all mood entries
    private fun retrieveAllMoodEntries(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/moodTracking").asJson()
    }

    // Helper function to retrieve mood entries by sleep tracking id
    private fun retrieveMoodEntriesBySleepTrackingId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/sleepTracking/${id}/moodTracking").asJson()
    }

    // Helper function to retrieve a mood entry by user id
    private fun retrieveMoodEntriesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/moodTracking").asJson()
    }

    // Helper function to retrieve a mood entry by mood entry id
    private fun retrieveMoodEntryByMoodEntryId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/moodTracking/$id").asJson()
    }

    // Helper function to delete a mood entry by mood entry id
    private fun deleteMoodEntryByMoodEntryId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/moodTracking/$id").asString()
    }

    // Helper function to update a mood entry
    private fun updateMoodEntry(
        id: Int,
        date: DateTime,
        mood: String,
        rating: Int,
        notes: String,
        userId: Int,
        sleepId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/moodTracking/$id")
            .body(
                """
            {
                "date": "$date",
                "mood": "$mood",
                "rating": $rating,
                "notes": "$notes",
                "userId": $userId,
                "sleepId": $sleepId
            }
            """.trimIndent()
            )
            .asJson()
    }

    // Helper function to add a mood entry
    private fun addMoodEntry(
        date: DateTime,
        mood: String,
        rating: Int,
        notes: String,
        userId: Int,
        sleepId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/moodTracking")
            .body(
                """
            {
                "date": "$date",
                "mood": "$mood",
                "rating": $rating,
                "notes": "$notes",
                "userId": $userId,
                "sleepId": $sleepId
            }
            """.trimIndent()
            )
            .asJson()
    }
}