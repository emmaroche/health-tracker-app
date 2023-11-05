package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    // Get all users
    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it)) }
        }
        return userList
    }

    // Find by user ID
    fun findById(id: Int): User?{
        return transaction {
            Users.select {
                Users.id eq id}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    // Save user
    fun save(user: User) : Int {
        return transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
            } get Users.id
        }
    }

    // Find user by email
    fun findByEmail(email: String) :User?{
        return transaction {
            Users.select {
                Users.email eq email}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    // Delete user
    fun delete(id: Int): Int {
        return transaction {
            Users.deleteWhere {
                Users.id eq id
            }
        }
    }

    // Update user
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


