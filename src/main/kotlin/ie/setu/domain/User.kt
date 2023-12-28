package ie.setu.domain

/**
 * Represents a user with basic details such as ID, name, and email.
 *
 * @property id Unique identifier for the user.
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property phoneNumber The phone number of the user.
 * @property address The home address of the user.
 */
data class User(
    var id: Int,
    var name: String,
    var email: String,
    var phoneNumber: Int,
    var address: String
)
