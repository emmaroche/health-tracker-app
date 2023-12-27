package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `Users` table in the database.
 * It provides methods to perform CRUD operations on user data.
 */
class UserDAO {

    /**
     * Get all users from the database.
     *
     * @return List of users.
     */
    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    /**
     * Find a user by user ID.
     *
     * @param id The ID of the user to find.
     * @return The found user, or null if not found.
     */
    fun findById(id: Int): User? {
        return transaction {
            Users.select {
                Users.id eq id
            }
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }

    /**
     * Save a user to the database.
     *
     * @param user The user to save.
     * @return The ID of the saved user.
     */
    fun save(user: User): Int {
        return transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
            } get Users.id
        }
    }

    /**
     * Find a user by email.
     *
     * @param email The email address of the user to find.
     * @return The found user, or null if not found.
     */
    fun findByEmail(email: String): User? {
        return transaction {
            Users.select {
                Users.email eq email
            }
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }

    /**
     * Delete a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            Users.deleteWhere {
                Users.id eq id
            }
        }
    }

    /**
     * Update a user by ID.
     *
     * @param id The ID of the user to update.
     * @param user The updated user information.
     * @return The number of updated rows.
     */
    fun update(id: Int, user: User): Int {
        return transaction {
            Users.update({
                Users.id eq id
            }) {
                it[name] = user.name
                it[email] = user.email
            }
        }
    }
}
