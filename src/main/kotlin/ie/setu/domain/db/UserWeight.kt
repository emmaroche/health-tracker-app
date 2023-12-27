package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * Represents the database table for storing user current weight records.
 */
object UserWeight : Table("userWeight") {
    val id = integer("id").autoIncrement().primaryKey()
    val currentWeight = double("current_weight")
    val currentWeightTimestamp = datetime("current_timestamp")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    // Reference to WeightGoals table
    val wgId = integer("weight_id").references(WeightGoals.id, onDelete = ReferenceOption.CASCADE)
}
