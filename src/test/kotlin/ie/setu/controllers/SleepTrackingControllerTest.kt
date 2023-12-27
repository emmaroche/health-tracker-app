package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.SleepEntry
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
class SleepTrackingControllerTest {

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
    inner class ReadSleepEntries {

        @Test
        fun `get all sleep entries from the database returns 200 or 404 response`() {
            val response = retrieveAllSleepEntries()
            if (response.status == 200) {
                val retrievedEntries = jsonNodeToObject<Array<SleepEntry>>(response)
                assertNotEquals(0, retrievedEntries.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all sleep entries by user id when user and sleep entries exist returns 200 response`() {
            // Arrange - add a user and 3 associated sleep entries that we plan to retrieve
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, addedUser.id
            )
            addSleepEntry(
                sleepTracking[1].date, sleepTracking[1].duration,
                sleepTracking[1].quality, sleepTracking[1].notes, addedUser.id
            )
            addSleepEntry(
                sleepTracking[2].date, sleepTracking[2].duration,
                sleepTracking[2].quality, sleepTracking[2].notes, addedUser.id
            )

            // Act & Assert - retrieve the three added sleep entries by user id
            val response = retrieveSleepEntriesByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedEntries = jsonNodeToObject<Array<SleepEntry>>(response)
            assertEquals(3, retrievedEntries.size)

            // After - delete the added user and assert a 204 is returned (sleep entries are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all sleep entries by user id when no sleep entries exist returns 404 response`() {
            // Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            // Act & Assert - retrieve the sleep entries by user id
            val response = retrieveSleepEntriesByUserId(addedUser.id)
            assertEquals(404, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all sleep entries by user id when no user exists returns 404 response`() {
            // Arrange
            val userId = -1

            // Act & Assert - retrieve sleep entries by user id
            val response = retrieveSleepEntriesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get sleep entry by sleep entry id when no sleep entry exists returns 404 response`() {
            // Arrange
            val entryId = -1

            // Act & Assert - attempt to retrieve the sleep entry by sleep entry id
            val response = retrieveSleepEntryBySleepEntryId(entryId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get sleep entry by sleep entry id when sleep entry exists returns 200 response`() {
            // Arrange - add a user and associated sleep entry
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addEntryResponse = addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, addedUser.id
            )
            assertEquals(201, addEntryResponse.status)
            val addedEntry = jsonNodeToObject<SleepEntry>(addEntryResponse)

            // Act & Assert - retrieve the sleep entry by sleep entry id
            val response = retrieveSleepEntryBySleepEntryId(addedEntry.id)
            assertEquals(200, response.status)

            // After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class CreateSleepEntries {

        @Test
        fun `add a sleep entry when a user exists for it, returns a 201 response`() {

            // Arrange - add a user and an associated sleep entry that we plan to do a delete on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())

            val addEntryResponse = addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, addedUser.id
            )
            assertEquals(201, addEntryResponse.status)

            // After - delete the user (sleep entry will cascade delete in the database)
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add a sleep entry when no user exists for it, returns a 404 response`() {

            // Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            val addEntryResponse = addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, userId
            )
            assertEquals(404, addEntryResponse.status)
        }
    }

    @Nested
    inner class UpdateSleepEntries {

        @Test
        fun `updating a sleep entry by sleep entry id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val entryId = -1

            // Arrange - check there is no user for -1 id
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            // Act & Assert - attempt to update the details of a sleep entry/user that doesn't exist
            assertEquals(
                404, updateSleepEntry(
                    entryId, updatedDate, updatedDuration2,
                    updatedQuality, updatedNotes, userId
                ).status
            )
        }

        @Test
        fun `updating a sleep entry by sleep entry id when it exists, returns 204 response`() {

            // Arrange - add a user and an associated sleep entry that we plan to do an update on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addEntryResponse = addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, addedUser.id
            )
            assertEquals(201, addEntryResponse.status)
            val addedEntry = jsonNodeToObject<SleepEntry>(addEntryResponse)

            // Act & Assert - update the added sleep entry and assert a 204 is returned
            val updatedEntryResponse = updateSleepEntry(
                addedEntry.id, updatedDate, updatedDuration2,
                updatedQuality, updatedNotes, addedUser.id
            )
            assertEquals(204, updatedEntryResponse.status)

            // Assert that the individual fields were all updated as expected
            val retrievedEntryResponse = retrieveSleepEntryBySleepEntryId(addedEntry.id)
            val updatedEntry = jsonNodeToObject<SleepEntry>(retrievedEntryResponse)
            assertEquals(updatedDate, updatedEntry.date)
            assertEquals(updatedDuration2, updatedEntry.duration)
            assertEquals(updatedQuality, updatedEntry.quality)
            assertEquals(updatedNotes, updatedEntry.notes)

            // After - delete the user
            testUtilities.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteSleepEntries {

        @Test
        fun `deleting sleep entries by user id when it doesn't exist, returns a 404 response`() {
            // Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteSleepEntryByUserId(-1).status)
        }

        @Test
        fun `deleting all sleep entries by user id when it exists, returns a 204 response`() {

            // Arrange - add a user and 3 associated sleep entries that we plan to do a cascade delete
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail).body.toString())
            val addEntryResponse1 = addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, addedUser.id
            )
            assertEquals(201, addEntryResponse1.status)
            val addEntryResponse2 = addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, addedUser.id
            )
            assertEquals(201, addEntryResponse2.status)
            val addEntryResponse3 = addSleepEntry(
                sleepTracking[0].date, sleepTracking[0].duration,
                sleepTracking[0].quality, sleepTracking[0].notes, addedUser.id
            )
            assertEquals(201, addEntryResponse3.status)

            // Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)

            // Act & Assert - attempt to retrieve the deleted sleep entries
            val addedEntry1 = jsonNodeToObject<SleepEntry>(addEntryResponse1)
            val addedEntry2 = jsonNodeToObject<SleepEntry>(addEntryResponse2)
            val addedEntry3 = jsonNodeToObject<SleepEntry>(addEntryResponse3)
            assertEquals(404, retrieveSleepEntryBySleepEntryId(addedEntry1.id).status)
            assertEquals(404, retrieveSleepEntryBySleepEntryId(addedEntry2.id).status)
            assertEquals(404, retrieveSleepEntryBySleepEntryId(addedEntry3.id).status)
        }
    }

    //--------------------------------------------------------------------------------------
    // HELPER METHODS
    //--------------------------------------------------------------------------------------

    // Helper function to retrieve all sleep entries
    private fun retrieveAllSleepEntries(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/sleepTracking").asJson()
    }

    // Helper function to retrieve a sleep entry by user id
    private fun retrieveSleepEntriesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/sleepTracking").asJson()
    }

    // Helper function to retrieve a sleep entry by sleep entry id
    private fun retrieveSleepEntryBySleepEntryId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/sleepTracking/${id}").asJson()
    }

    // Helper function to delete a sleep entry
    private fun deleteSleepEntryByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/sleepTracking/$id").asString()
    }

    // Helper function to update a sleep entry
    private fun updateSleepEntry(
        id: Int,
        date: DateTime,
        duration: Int,
        quality: String,
        notes: String,
        userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/sleepTracking/$id")
            .body(
                """
            {
                "date": "$date",
                "duration": $duration,
                "quality": "$quality",
                "notes": "$notes",
                "userId": $userId
            }
            """.trimIndent()
            )
            .asJson()
    }

    // Helper function to add a sleep entry
    private fun addSleepEntry(
        date: DateTime,
        duration: Int,
        quality: String,
        notes: String,
        userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/sleepTracking")
            .body(
                """
            {
                "date": "$date",
                "duration": $duration,
                "quality": "$quality",
                "notes": "$notes",
                "userId": $userId
            }
            """.trimIndent()
            )
            .asJson()
    }
}
