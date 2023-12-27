package ie.setu.domain

import org.joda.time.DateTime

/**
 * Represents a weight goal set by a user.
 *
 * @property id Unique identifier for the weight goal.
 * @property type The type or category of the weight goal.
 * @property startingWeight The starting weight when the goal was set.
 * @property startingWeightTimestamp The timestamp when the starting weight was recorded.
 * @property targetWeight The target weight the user aims to achieve.
 * @property weeklyGoal The weekly weight loss or gain goal.
 * @property deadline The deadline or target date to achieve the weight goal.
 * @property userId The ID of the user setting the weight goal.
 * @property actId The ID of the associated activity for tracking progress.
 */
data class WeightGoal(
    var id: Int,
    val type: String,
    val startingWeight: Double,
    val startingWeightTimestamp: DateTime,
    val targetWeight: Double,
    val weeklyGoal: Double,
    val deadline: DateTime,
    var userId: Int,
    var actId: Int
)
