package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object FitnessGoals : Table("fitnessGoals") {
    val id = integer("id").autoIncrement().primaryKey()
    val type = varchar("type", 50)
    val workoutsPerWeek = integer("workouts_per_week")
    val minutesOfWorkouts = integer("minutes_of_workouts")
    val calorieBurningGoalDuringExercise = double("calorie_burning_goal")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}

