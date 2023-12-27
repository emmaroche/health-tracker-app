package ie.setu.domain

import org.joda.time.DateTime

/**
 * Represents a sleep entry capturing details about sleep duration, quality, and additional notes.
 *
 * @property id Unique identifier for the sleep entry.
 * @property date The date and time of the sleep entry.
 * @property duration The duration of sleep in minutes.
 * @property quality The quality of sleep, represented as a String.
 * @property notes Additional notes or comments related to the sleep entry.
 * @property userId The ID of the user associated with the sleep entry.
 */
data class SleepEntry(
    var id: Int,
    val date: DateTime,
    val duration: Int,
    val quality: String,
    val notes: String,
    var userId: Int
)
