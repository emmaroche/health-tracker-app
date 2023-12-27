package ie.setu.domain

/**
 * Represents a nutrition goal for a user, capturing goals for protein, fiber, calories, carbohydrates, and fat.
 *
 * @property id Unique identifier for the nutrition goal.
 * @property type The type or category of the nutrition goal.
 * @property proteinGoal The target protein intake goal.
 * @property fibreGoal The target fiber intake goal.
 * @property calorieGoal The target calorie intake goal.
 * @property carbsGoal The target carbohydrate intake goal.
 * @property fatGoal The target fat intake goal.
 * @property userId The ID of the user to whom the nutrition goal belongs.
 * @property fitnessId The ID of the associated fitness goal.
 * @property weightId The ID of the associated weight goal.
 */
data class NutritionGoal(
    var id: Int,
    val type: String,
    val proteinGoal: Double,
    val fibreGoal: Double,
    val calorieGoal: Double,
    val carbsGoal: Double,
    val fatGoal: Double,
    var userId: Int,
    var fitnessId: Int,
    var weightId: Int
)
