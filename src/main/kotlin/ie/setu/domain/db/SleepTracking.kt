//package ie.setu.domain.db
//
//import org.jetbrains.exposed.sql.ReferenceOption
//import org.jetbrains.exposed.sql.Table
//
//object SleepTracking : Table("sleep-entries") {
//    val id = integer("id").autoIncrement().primaryKey()
//    val date = datetime("date")
//    val duration = integer("duration")
//    val quality = varchar("quality", 50)
//    val notes = varchar("notes", 200)
//    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
//}