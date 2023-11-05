package ie.setu.utils

import ie.setu.helpers.ServerContainer
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class TestUtilities {

    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    fun addUser(name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    // Helper function to delete a test user from the database
    fun deleteUser(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id").asString()
    }

    // Helper function to retrieve a test user from the database by email
    fun retrieveUserByEmail(email: String): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

    // Helper function to retrieve a test user from the database by id
    fun retrieveUserById(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }
}