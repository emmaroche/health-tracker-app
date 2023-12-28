package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.HealthRecord
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
class HealthRecordControllerTest {

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
    inner class ReadHealthRecords {

        @Test
        fun `get all health records from the database returns 200 or 404 response`() {
            val response = retrieveAllHr()
            if (response.status == 200) {
                val retrievedHr = jsonNodeToObject<Array<HealthRecord>>(response)
                assertNotEquals(0, retrievedHr.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all health records by user id when user and health records  exists returns 200 response`() {
            //Arrange - add a user and 3 associated health records that we plan to retrieve
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())
            addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, addedUser.id
            )
            addHealthRecord(
                healthRecords[1].timestamp, healthRecords[1].firstName,
                healthRecords[1].lastName, healthRecords[1].sex,
                healthRecords[1].dob, healthRecords[1].weight,
                healthRecords[1].height, healthRecords[1].bloodType,
                healthRecords[1].allergies, healthRecords[1].medicalConditions,
                healthRecords[1].medications, healthRecords[1].notes, addedUser.id
            )
            addHealthRecord(
                healthRecords[2].timestamp, healthRecords[2].firstName,
                healthRecords[2].lastName, healthRecords[2].sex,
                healthRecords[2].dob, healthRecords[2].weight,
                healthRecords[2].height, healthRecords[2].bloodType,
                healthRecords[2].allergies, healthRecords[2].medicalConditions,
                healthRecords[2].medications, healthRecords[2].notes, addedUser.id
            )

            //Assert and Act - retrieve the three added health records by user id
            val response = retrieveHrByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedHr = jsonNodeToObject<Array<HealthRecord>>(response)
            assertEquals(3, retrievedHr.size)

            //After - delete the added user and assert a 204 is returned (health records are cascade deleted)
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all health records by user id when no health records exist returns 404 response`() {
            //Arrange - add a user
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

            //Assert and Act - retrieve the health records by user id
            val response = retrieveHrByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }


        @Test
        fun `get all health records by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve health records by user id
            val response = retrieveHrByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get health record by health record id when no health record exists returns 404 response`() {
            //Arrange
            val hrId = -1
            //Assert and Act - attempt to retrieve the health record by health record id
            val response = retrieveHrByHrId(hrId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get health record by health record id when health record exists returns 200 response`() {
            //Arrange - add a user and associated health record
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())
            val addHrResponse = addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, addedUser.id
            )
            assertEquals(201, addHrResponse.status)
            val addedHr = jsonNodeToObject<HealthRecord>(addHrResponse)

            //Act & Assert - retrieve the health record by health record id
            val response = retrieveHrByHrId(addedHr.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class CreateHealthRecords {

        @Test
        fun `add a health record when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated health record that we plan to do a delete on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())

            val addHrResponse = addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, addedUser.id
            )
            assertEquals(201, addHrResponse.status)

            //After - delete the user (health record will cascade delete in the database)
            testUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add a health record when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            val addHrResponse = addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, userId
            )
            assertEquals(404, addHrResponse.status)
        }
    }

    @Nested
    inner class UpdateHealthRecords {

        @Test
        fun `updating a health record by health record id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val hrId = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, testUtilities.retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of a health record/user that doesn't exist
            assertEquals(
                404, updateHealthRecord(
                    hrId, updatedTimestamp, updatedFirstname,
                    updatedLastname, updatedSex, updatedDob, updatedWeight,
                    updatedHeight, updatedBloodType, updatedAllergies, updatedMedicalConditions,
                    updatedMedications, updatedNote, userId
                ).status
            )
        }

        @Test
        fun `updating a health record by health record id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated health record that we plan to do an update on
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())
            val addHrResponse = addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, addedUser.id
            )
            assertEquals(201, addHrResponse.status)
            val addedHr = jsonNodeToObject<HealthRecord>(addHrResponse)

            //Act & Assert - update the added health record and assert a 204 is returned
            val updatedHrResponse = updateHealthRecord(
                addedHr.id, updatedTimestamp, updatedFirstname,
                updatedLastname, updatedSex, updatedDob, updatedWeight,
                updatedHeight, updatedBloodType, updatedAllergies, updatedMedicalConditions,
                updatedMedications, updatedNote, addedUser.id
            )
            assertEquals(204, updatedHrResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedHrResponse = retrieveHrByHrId(addedHr.id)
            val updatedHr = jsonNodeToObject<HealthRecord>(retrievedHrResponse)
            assertEquals(updatedTimestamp, updatedHr.timestamp)
            assertEquals(updatedFirstname, updatedHr.firstName)
            assertEquals(updatedLastname, updatedHr.lastName)
            assertEquals(updatedSex, updatedHr.sex)
            assertEquals(updatedDob, updatedHr.dob)
            assertEquals(updatedWeight, updatedHr.weight)
            assertEquals(updatedHeight, updatedHr.height)
            assertEquals(updatedBloodType, updatedHr.bloodType)
            assertEquals(updatedAllergies, updatedHr.allergies)
            assertEquals(updatedMedicalConditions, updatedHr.medicalConditions)
            assertEquals(updatedMedications, updatedHr.medications)
            assertEquals(updatedNote, updatedHr.notes)

            //After - delete the user
            testUtilities.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteHealthRecords {

        @Test
        fun `deleting health records by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteHrByUserId(-1).status)
        }

        @Test
        fun `deleting all health records by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated health records that we plan to do a cascade delete
            val addedUser: User = jsonToObject(testUtilities.addUser(validName, validEmail, validPhone, validAddress).body.toString())
            val addHrResponse1 = addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, addedUser.id
            )
            assertEquals(201, addHrResponse1.status)
            val addHrResponse2 = addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, addedUser.id
            )
            assertEquals(201, addHrResponse2.status)
            val addHrResponse3 = addHealthRecord(
                healthRecords[0].timestamp, healthRecords[0].firstName,
                healthRecords[0].lastName, healthRecords[0].sex,
                healthRecords[0].dob, healthRecords[0].weight,
                healthRecords[0].height, healthRecords[0].bloodType,
                healthRecords[0].allergies, healthRecords[0].medicalConditions,
                healthRecords[0].medications, healthRecords[0].notes, addedUser.id
            )
            assertEquals(201, addHrResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, testUtilities.deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted health records
            val addedHr1 = jsonNodeToObject<HealthRecord>(addHrResponse1)
            val addedHr2 = jsonNodeToObject<HealthRecord>(addHrResponse2)
            val addedHr3 = jsonNodeToObject<HealthRecord>(addHrResponse3)
            assertEquals(404, retrieveHrByHrId(addedHr1.id).status)
            assertEquals(404, retrieveHrByHrId(addedHr2.id).status)
            assertEquals(404, retrieveHrByHrId(addedHr3.id).status)
        }
    }

    //--------------------------------------------------------------------------------------
    // HELPER METHODS
    //--------------------------------------------------------------------------------------

    //Helper function to retrieve all health records
    private fun retrieveAllHr(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/healthRecords").asJson()
    }

    //Helper function to retrieve a health record by user id
    private fun retrieveHrByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/healthRecords").asJson()
    }

    //Helper function to retrieve a health record by health record id
    private fun retrieveHrByHrId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/healthRecords/${id}").asJson()
    }

    //Helper function to delete a health record
    private fun deleteHrByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/healthRecords/$id").asString()
    }

    //Helper function to update a health record
    private fun updateHealthRecord(
        id: Int,
        timestamp: DateTime,
        firstName: String,
        lastName: String,
        sex: String,
        dob: DateTime,
        weight: Double,
        height: Int,
        bloodType: String,
        allergies: String,
        medicalConditions: String,
        medications: String,
        notes: String,
        userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/healthRecords/$id")
            .body(
                """
            {
                "timestamp": "$timestamp",
                "firstName": "$firstName",
                "lastName": "$lastName",
                "sex": "$sex",
                "dob": "$dob",
                "weight": $weight,
                "height": $height,
                "bloodType": "$bloodType",
                "allergies": "$allergies",
                "medicalConditions": "$medicalConditions",
                "medications": "$medications",
                "notes": "$notes",
                "userId": $userId
            }
            """.trimIndent()
            )
            .asJson()
    }

    //Helper function to add a health record
    private fun addHealthRecord(
        timestamp: DateTime,
        firstName: String,
        lastName: String,
        sex: String,
        dob: DateTime,
        weight: Double,
        height: Int,
        bloodType: String,
        allergies: String,
        medicalConditions: String,
        medications: String,
        notes: String,
        userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/healthRecords")
            .body(
                """
            {
                "timestamp": "$timestamp",
                "firstName": "$firstName",
                "lastName": "$lastName",
                "sex": "$sex",
                "dob": "$dob",
                "weight": $weight,
                "height": $height,
                "bloodType": "$bloodType",
                "allergies": "$allergies",
                "medicalConditions": "$medicalConditions}",
                "medications": "$medications}",
                "notes": "$notes}",
                "userId": $userId}
            }
            """.trimIndent()
            )
            .asJson()
    }

}


