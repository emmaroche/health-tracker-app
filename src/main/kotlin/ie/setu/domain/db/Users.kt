package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

/**
 * Represents the database table for storing user records.
 */
object Users : Table("users") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
}