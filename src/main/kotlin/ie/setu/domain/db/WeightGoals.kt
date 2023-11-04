package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object WeightGoals : Table("weightGoals") {
    val id = integer("id").autoIncrement().primaryKey()
    val type = varchar("type", 50)
    val startingWeight = double("startingWeight")
    val startingWeightTimestamp = varchar("startingWeightTimestamp", 100)
    val currentWeight = double("currentWeight")
    val targetWeight = double("targetWeight")
    val weeklyGoal = double("weeklyGoal")
    val deadline = datetime("deadline")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}
