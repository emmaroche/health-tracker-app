package ie.setu.domain

import org.joda.time.DateTime

data class MoodEntry(
    var id: Int,
    val date: DateTime,
    val mood: String,
    val rating: Int,
    val notes: String,
    var userId: Int
)
