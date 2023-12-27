package ie.setu.domain

import org.joda.time.DateTime

/**
 * Represents a mood entry for a user, capturing the user's mood, rating, and additional notes.
 *
 * @property id Unique identifier for the mood entry.
 * @property date The date and time when the mood entry was recorded.
 * @property mood The user's reported mood.
 * @property rating The rating given to the mood entry, indicating the intensity or satisfaction level.
 * @property notes Additional notes or comments related to the mood entry.
 * @property userId The ID of the user to whom the mood entry belongs.
 * @property sleepId The optional ID of the associated sleep entry, if applicable.
 */
data class MoodEntry(
    var id: Int,
    val date: DateTime,
    val mood: String,
    val rating: Int,
    val notes: String,
    var userId: Int,
    var sleepId: Int?
)
