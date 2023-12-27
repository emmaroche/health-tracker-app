package ie.setu.domain

import org.joda.time.DateTime

/**
 * Represents an activity performed by a user.
 *
 * @property id Unique identifier for the activity.
 * @property description A brief description of the activity.
 * @property duration The duration of the activity in hours.
 * @property calories The number of calories burned during the activity.
 * @property started The start time of the activity as a [DateTime] object.
 * @property userId The user ID associated with the activity.
 * @property fitnessId The fitness goal ID associated with the activity.
 */
data class Activity(
    var id: Int,
    var description: String,
    var duration: Double,
    var calories: Int,
    var started: DateTime,
    var userId: Int,
    var fitnessId: Int
)
