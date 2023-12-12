package ie.setu.domain

data class NutritionGoal(
    var id: Int,
    val type: String,
    val proteinGoal: Double,
    val fibreGoal: Double,
    val calorieGoal: Double,
    val carbsGoal: Double,
    val fatGoal: Double,
    var userId: Int
)
