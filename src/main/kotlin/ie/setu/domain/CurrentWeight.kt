package ie.setu.domain

import org.joda.time.DateTime

/**
 * Represents the current weight entry of a user.
 *
 * @property id Unique identifier for the current weight entry.
 * @property currentWeight The current weight of the user.
 * @property currentWeightTimestamp The timestamp when the current weight entry was recorded as a [DateTime] object.
 * @property weightGoalId The ID of the associated weight goal.
 * @property userId The ID of the user for whom the weight entry is recorded.
 */
data class CurrentWeight(
    var id: Int,
    val currentWeight: Double,
    val currentWeightTimestamp: DateTime,
    val weightGoalId: Int, // Reference to the associated weight goal
    val userId: Int
)
