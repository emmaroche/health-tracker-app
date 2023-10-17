package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object NutritionGoals : Table("nutrition-goals") {
    val id = integer("id").autoIncrement().primaryKey()
    val type = varchar("type", 50)
    val weeklyGoal = double("weekly_goal")
    val proteinGoal = double("protein_goal")
    val fibreGoal = double("fibre_goal")
    val calorieGoal = double("calorie_goal")
    val carbsGoal = double("carbs_goal")
    val fatGoal = double("fat_goal")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}