package ie.setu.domain.repository

import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.FitnessGoals
import ie.setu.utils.mapToFitnessGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `FitnessGoals` table in the database.
 * It provides methods to perform CRUD operations on fitness goal records.
 */
class FitnessGoalsDAO {

    /**
     * Get all fitness goals from the database.
     *
     * @return List of fitness goals.
     */
    fun getAll(): ArrayList<FitnessGoal> {
        val fgList: ArrayList<FitnessGoal> = arrayListOf()
        transaction {
            FitnessGoals.selectAll().map {
                fgList.add(mapToFitnessGoal(it))
            }
        }
        return fgList
    }

    /**
     * Find fitness goals by user ID.
     *
     * @param userId The ID of the user to find fitness goals for.
     * @return List of fitness goals for the specified user.
     */
    fun findByUserId(userId: Int): List<FitnessGoal> {
        return transaction {
            FitnessGoals
                .select { FitnessGoals.userId eq userId }
                .map { mapToFitnessGoal(it) }
        }
    }

    /**
     * Find a specific fitness goal by goal ID.
     *
     * @param id The ID of the fitness goal to find.
     * @return The found fitness goal, or null if not found.
     */
    fun findByGoalId(id: Int): FitnessGoal? {
        return transaction {
            FitnessGoals
                .select { FitnessGoals.id eq id }
                .map { mapToFitnessGoal(it) }
                .firstOrNull()
        }
    }

    /**
     * Save fitness goals to the database.
     *
     * @param fitnessGoal The fitness goal to save.
     * @return The ID of the saved fitness goal.
     */
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

    /**
     * Delete a fitness goal by goal ID.
     *
     * @param id The ID of the fitness goal to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            FitnessGoals.deleteWhere {
                FitnessGoals.id eq id
            }
        }
    }

    /**
     * Update a fitness goal by goal ID.
     *
     * @param goalId The ID of the fitness goal to update.
     * @param fitnessGoalToUpdate The updated fitness goal information.
     * @return The number of updated rows.
     */
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
