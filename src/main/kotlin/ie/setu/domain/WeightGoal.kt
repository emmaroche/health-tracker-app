package ie.setu.domain

import org.joda.time.DateTime

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
