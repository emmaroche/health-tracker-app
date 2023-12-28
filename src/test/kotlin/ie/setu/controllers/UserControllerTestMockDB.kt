package ie.setu.controllers

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.helpers.*
import ie.setu.utils.TestUtilities
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTestMockDB {

    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    private val testUtilities = TestUtilities()

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Test
    fun `getting a user by id when id exists, returns a 200 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Arrange - add the user to the h2 database
            val addResponse = testUtilities.addUser(validName, validEmail, validPhone, validAddress)
            assertEquals(201, addResponse.status)

            // Assert - retrieve the user from the fake database
            val retrieveResponse = testUtilities.retrieveUserByEmail(validEmail)

            // Verify the return code and the contents of the retrieved user
            assertEquals(200, retrieveResponse.status)
            val retrievedUser: User = jsonToObject(retrieveResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)
            assertEquals(validPhone, retrievedUser.phoneNumber)  // Fix this assertion
            assertEquals(validAddress, retrievedUser.address)  // Fix this assertion
        }
    }

    @Test
    fun `getting a user by id when id does not exist, returns a 404 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Act - retrieve a user with an invalid id from the fake database
            val retrieveResponse = testUtilities.retrieveUserById(123)

            // Assert - verify the return code is 404
            assertEquals(404, retrieveResponse.status)
        }
    }

    @Test
    fun `getting all users when users exist, returns a 200 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Arrange - add users to the h2 database
            val user1Response = testUtilities.addUser(validName, validEmail, validPhone, validAddress)
            assertEquals(201, user1Response.status)

            val user2Response = testUtilities.addUser("John Doe", "john.doe@example.com", 1234567890, "123 Main St")
            assertEquals(201, user2Response.status)

            // Act - retrieve all users from the fake database
            val retrieveAllResponse = retrieveAllUsers()

            // Assert - verify the return code and the contents of the retrieved users
            assertEquals(200, retrieveAllResponse.status)
            val retrievedUsers: List<User> = jsonToObject(retrieveAllResponse.body.toString())
            assertEquals(2, retrievedUsers.size)
        }
    }


    @Test
    fun `getting all users when no users exist, returns a 404 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Act - retrieve all users from the empty fake database
            val retrieveAllResponse = retrieveAllUsers()

            // Assert - verify the return code is 404
            assertEquals(404, retrieveAllResponse.status)
        }
    }

    @Test
    fun `deleting a user by id when id exists, returns a 204 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Arrange - add a user to the h2 database
            val addResponse = testUtilities.addUser(validName, validEmail, validPhone, validAddress)
            assertEquals(201, addResponse.status)

            // Act - delete the user from the fake database
            val deleteResponse = testUtilities.deleteUser(1)

            // Assert - verify the return code is 204
            assertEquals(204, deleteResponse.status)
        }
    }

    @Test
    fun `deleting a user by ID when ID does not exist, returns a 404 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Act - delete a non-existent user from the fake database
            val deleteResponse = testUtilities.deleteUser(123)

            // Assert - verify the return code is 404
            assertEquals(404, deleteResponse.status)
        }
    }

    @Test
    fun `updating a user with valid data, returns a 204 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Arrange - add a user to the h2 database
            val addResponse = testUtilities.addUser(validName, validEmail, validPhone, validAddress)
            assertEquals(201, addResponse.status)

            // Act - update the user in the fake database
            val updateUser = User(id = 1, name = "Updated User", email = "updated.user@example.com", phoneNumber = 98743210, address = "456 Updated St")
            val updateResponse = updateUser(1, updateUser.name, updateUser.email, updateUser.phoneNumber, updateUser.address)

            // Assert - verify the return code is 204
            assertEquals(204, updateResponse.status)
        }
    }

    @Test
    fun `updating a user with invalid data, returns a 404 response`() {
        transaction {
            // Arrange – create a fake users table in the h2 database
            SchemaUtils.create(Users)

            // Act - update a user with invalid data in the fake database
            val updateUser = User(id = 1, name = "", email = "invalid-email", phoneNumber = 95849584, address = "invalid-address")
            val updateResponse = updateUser(1, updateUser.name, updateUser.email, updateUser.phoneNumber, updateUser.address)

            // Assert - verify the return code is 404
            assertEquals(404, updateResponse.status)
        }
    }


    private fun updateUser(id: Int, name: String, email: String, phoneNumber: Int, address: String): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/users/$id")
            .body("{\"name\":\"$name\", \"email\":\"$email\", \"phoneNumber\": $phoneNumber, \"address\": \"$address\"}")
            .asJson()
    }

    private fun retrieveAllUsers(): HttpResponse<String> {
        return Unirest.get("$origin/api/users").asString()
    }


}