package ie.setu.domain

import org.joda.time.DateTime

data class SleepEntry(
    var id: Int,
    val date: DateTime,
    val duration: Int,
    val quality: String,
    val notes: String,
    var userId: Int
)
