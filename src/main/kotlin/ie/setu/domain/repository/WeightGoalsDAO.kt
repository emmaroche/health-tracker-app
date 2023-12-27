package ie.setu.domain.repository

import ie.setu.domain.WeightGoal
import ie.setu.domain.db.WeightGoals
import ie.setu.utils.mapToWeightGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `WeightGoals` table in the database.
 * It provides methods to perform CRUD operations on weight goal data.
 */
class WeightGoalsDAO {

    /**
     * Get all weight goals from the database.
     *
     * @return List of weight goals.
     */
    fun getAll(): ArrayList<WeightGoal> {
        val wgList: ArrayList<WeightGoal> = arrayListOf()
        transaction {
            WeightGoals.selectAll().map {
                wgList.add(mapToWeightGoal(it))
            }
        }
        return wgList
    }

    /**
     * Find weight goals by user ID.
     *
     * @param userId The ID of the user to find weight goals for.
     * @return List of weight goals for the specified user.
     */
    fun findByUserId(userId: Int): List<WeightGoal> {
        return transaction {
            WeightGoals
                .select { WeightGoals.userId eq userId }
                .map { mapToWeightGoal(it) }
        }
    }

    /**
     * Find weight goals by activity ID.
     *
     * @param actId The ID of the activity associated with weight goals.
     * @return List of weight goals associated with the specified activity.
     */
    fun findByActivityId(actId: Int): List<WeightGoal> {
        return transaction {
            WeightGoals
                .select { WeightGoals.actId eq actId }
                .map { mapToWeightGoal(it) }
        }
    }

    /**
     * Find a specific weight goal by goal ID.
     *
     * @param id The ID of the weight goal to find.
     * @return The found weight goal, or null if not found.
     */
    fun findByGoalId(id: Int): WeightGoal? {
        return transaction {
            WeightGoals
                .select { WeightGoals.id eq id }
                .map { mapToWeightGoal(it) }
                .firstOrNull()
        }
    }

    /**
     * Find a specific weight goal type.
     *
     * @param type The type of weight goal to find.
     * @return The found weight goal, or null if not found.
     */
    fun findByType(type: String): WeightGoal? {
        return transaction {
            WeightGoals.select {
                WeightGoals.type eq type
            }
                .map { mapToWeightGoal(it) }
                .firstOrNull()
        }
    }

    /**
     * Save weight goals to the database.
     *
     * @param weightGoal The weight goal to save.
     * @return The ID of the saved weight goal.
     */
    fun save(weightGoal: WeightGoal): Int {
        return transaction {
            WeightGoals.insert {
                it[type] = weightGoal.type
                it[startingWeight] = weightGoal.startingWeight
                it[startingWeightTimestamp] = weightGoal.startingWeightTimestamp
                it[targetWeight] = weightGoal.targetWeight
                it[weeklyGoal] = weightGoal.weeklyGoal
                it[deadline] = weightGoal.deadline
                it[userId] = weightGoal.userId
                it[actId] = weightGoal.actId
            } get WeightGoals.id
        }
    }

    /**
     * Delete weight goal by ID.
     *
     * @param id The ID of the weight goal to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            WeightGoals.deleteWhere {
                WeightGoals.id eq id
            }
        }
    }

    /**
     * Update weight goal by goal ID.
     *
     * @param goalId The ID of the weight goal to update.
     * @param weightGoalToUpdate The updated weight goal information.
     * @return The number of updated rows.
     */
    fun updateWeightGoal(goalId: Int, weightGoalToUpdate: WeightGoal): Int {
        return transaction {
            WeightGoals.update({
                WeightGoals.id eq goalId
            }) {
                it[type] = weightGoalToUpdate.type
                it[startingWeight] = weightGoalToUpdate.startingWeight
                it[startingWeightTimestamp] = weightGoalToUpdate.startingWeightTimestamp
                it[targetWeight] = weightGoalToUpdate.targetWeight
                it[weeklyGoal] = weightGoalToUpdate.weeklyGoal
                it[deadline] = weightGoalToUpdate.deadline
                it[userId] = weightGoalToUpdate.userId
                it[actId] = weightGoalToUpdate.actId
            }
        }
    }
}
