package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * Represents the database table for storing nutrition goals.
 */
object NutritionGoals : Table("nutritionGoals") {
    val id = integer("id").autoIncrement().primaryKey()
    val type = varchar("type", 50)
    val proteinGoal = double("protein_goal")
    val fibreGoal = double("fibre_goal")
    val calorieGoal = double("calorie_goal")
    val carbsGoal = double("carbs_goal")
    val fatGoal = double("fat_goal")

    // Reference to the Users table
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    // Reference to the FitnessGoals table
    val fitnessId = integer("fitness_id").references(FitnessGoals.id, onDelete = ReferenceOption.CASCADE)

    // Reference to the WeightGoals table
    val weightId = integer("weight_id").references(WeightGoals.id, onDelete = ReferenceOption.CASCADE)
}
