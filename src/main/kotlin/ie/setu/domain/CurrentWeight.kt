package ie.setu.domain

import org.joda.time.DateTime

data class CurrentWeight(
    var id: Int,
    val currentWeight: Double,
    val currentWeightTimestamp: DateTime,
    val weightGoalId: Int, // Reference to the associated weight goal
    val userId: Int
)
