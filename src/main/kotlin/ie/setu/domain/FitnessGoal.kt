package ie.setu.domain

/**
 * Represents a fitness goal for a user.
 *
 * @property id Unique identifier for the fitness goal.
 * @property type The type of fitness goal (e.g., cardio, strength training).
 * @property workoutsPerWeek The number of workouts per week planned to achieve the fitness goal.
 * @property minutesOfWorkouts The total minutes of workouts planned per session.
 * @property calorieBurningGoalDuringExercise The calorie burning goal during exercise sessions.
 * @property userId The ID of the user for whom the fitness goal is set.
 */
data class FitnessGoal(
    var id: Int,
    val type: String,
    val workoutsPerWeek: Int,
    val minutesOfWorkouts: Int,
    val calorieBurningGoalDuringExercise: Double,
    var userId: Int
)
