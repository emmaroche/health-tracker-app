package ie.setu.domain.repository

import ie.setu.domain.WeightGoal
import ie.setu.domain.db.WeightGoals
import ie.setu.utils.mapToWeightGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class WeightGoalsDAO {

    // Get all weight goals
    fun getAll(): ArrayList<WeightGoal> {
        val wgList: ArrayList<WeightGoal> = arrayListOf()
        transaction {
            WeightGoals.selectAll().map {
                wgList.add(mapToWeightGoal(it))
            }
        }
        return wgList
    }

    // Find weight goals by user ID
    fun findByUserId(userId: Int): List<WeightGoal> {
        return transaction {
            WeightGoals
                .select { WeightGoals.userId eq userId }
                .map { mapToWeightGoal(it) }
        }
    }

    // Find weight goals by activity ID
    fun findByActivityId(actId: Int): List<WeightGoal> {
        return transaction {
            WeightGoals
                .select { WeightGoals.actId eq actId }
                .map { mapToWeightGoal(it) }
        }
    }

    // Find a specific weight goal by goal ID
    fun findByGoalId(id: Int): WeightGoal? {
        return transaction {
            WeightGoals
                .select { WeightGoals.id eq id }
                .map { mapToWeightGoal(it) }
                .firstOrNull()
        }
    }

    // Find a specific weight goal type
    fun findByType(type: String): WeightGoal? {
        return transaction {
            WeightGoals.select {
                WeightGoals.type eq type
            }
                .map { mapToWeightGoal(it) }
                .firstOrNull()
        }
    }

    // Save weight goals
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


    // Delete weight goal
    fun delete(id: Int): Int {
        return transaction {
            WeightGoals.deleteWhere {
                WeightGoals.id eq id
            }
        }
    }

    // Update weight goal
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
