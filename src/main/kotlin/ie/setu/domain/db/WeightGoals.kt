package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object WeightGoals : Table("weightGoals") {
    val id = integer("id").autoIncrement().primaryKey()
    val type = varchar("type", 50)
    val startingWeight = double("starting_weight")
    val startingWeightTimestamp = datetime("start_timestamp")
//    val currentWeight = double("current_weight") // data redundancy - table that goes in middle of user and weight goals
    val targetWeight = double("target_value")
    val weeklyGoal = double("weekly_goal")
    val deadline = datetime("deadline")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}
