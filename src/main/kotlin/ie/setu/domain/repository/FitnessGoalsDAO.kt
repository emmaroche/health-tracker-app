package ie.setu.domain.repository

import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.FitnessGoals
import ie.setu.utils.mapToFitnessGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FitnessGoalsDAO {

    // Get all fitness goals
    fun getAll(): ArrayList<FitnessGoal> {
        val fgList: ArrayList<FitnessGoal> = arrayListOf()
        transaction {
            FitnessGoals.selectAll().map {
                fgList.add(mapToFitnessGoal(it))
            }
        }
        return fgList
    }

    // Find fitness goals by user ID
    fun findByUserId(userId: Int): List<FitnessGoal> {
        return transaction {
            FitnessGoals
                .select { FitnessGoals.userId eq userId }
                .map { mapToFitnessGoal(it) }
        }
    }

    // Find a specific fitness goal by goal ID
    fun findByGoalId(id: Int): FitnessGoal? {
        return transaction {
            FitnessGoals
                .select { FitnessGoals.id eq id }
                .map { mapToFitnessGoal(it) }
                .firstOrNull()
        }
    }


    // Find a specific fitness goal type
    fun findByType(type: String): FitnessGoal? {
        return transaction {
            FitnessGoals.select {
                FitnessGoals.type eq type
            }
                .map { mapToFitnessGoal(it) }
                .firstOrNull()
        }
    }

    // Save fitness goals
    fun save(fitnessGoal: FitnessGoal): Int {
        return transaction {
            FitnessGoals.insert {
                it[type] = fitnessGoal.type
                it[workoutsPerWeek] = fitnessGoal.workoutsPerWeek
                it[minutesOfWorkouts] = fitnessGoal.minutesOfWorkouts
                it[calorieBurningGoalDuringExercise] = fitnessGoal.calorieBurningGoalDuringExercise
                it[userId] = fitnessGoal.userId
            } get FitnessGoals.id
        }
    }

    // Delete fitness goal
    fun delete(id: Int): Int {
        return transaction {
            FitnessGoals.deleteWhere {
                FitnessGoals.id eq id
            }
        }
    }

    // Update fitness goal
    fun updateFitnessGoal(goalId: Int, fitnessGoalToUpdate: FitnessGoal): Int {
        return transaction {
            FitnessGoals.update({
                FitnessGoals.id eq goalId
            }) {
                it[type] = fitnessGoalToUpdate.type
                it[workoutsPerWeek] = fitnessGoalToUpdate.workoutsPerWeek
                it[minutesOfWorkouts] = fitnessGoalToUpdate.minutesOfWorkouts
                it[calorieBurningGoalDuringExercise] = fitnessGoalToUpdate.calorieBurningGoalDuringExercise
                it[userId] = fitnessGoalToUpdate.userId
            }
        }
    }
}
