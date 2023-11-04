package ie.setu.domain
data class FitnessGoal(
    var id: Int,
    val type: String,
    val workoutsPerWeek: Int,
    val minutesOfWorkouts: Int,
    val calorieBurningGoalDuringExercise: Double,
    var userId: Int
)
