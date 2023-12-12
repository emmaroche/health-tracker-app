package ie.setu.domain.repository

import ie.setu.domain.NutritionGoal
import ie.setu.domain.db.NutritionGoals
import ie.setu.utils.mapToNutritionGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class NutritionGoalsDAO {

    // Get all nutrition goals
    fun getAll(): ArrayList<NutritionGoal> {
        val ngList: ArrayList<NutritionGoal> = arrayListOf()
        transaction {
            NutritionGoals.selectAll().map {
                ngList.add(mapToNutritionGoal(it))
            }
        }
        return ngList
    }

    // Find nutrition goals by user ID
    fun findByUserId(userId: Int): List<NutritionGoal> {
        return transaction {
            NutritionGoals
                .select { NutritionGoals.userId eq userId }
                .map { mapToNutritionGoal(it) }
        }
    }

    // Find a specific nutrition goal by goal ID
    fun findByGoalId(id: Int): NutritionGoal? {
        return transaction {
            NutritionGoals
                .select { NutritionGoals.id eq id }
                .map { mapToNutritionGoal(it) }
                .firstOrNull()
        }
    }

    // Find a specific nutrition goal type
    fun findByType(type: String): NutritionGoal? {
        return transaction {
            NutritionGoals.select {
                NutritionGoals.type eq type
            }
                .map { mapToNutritionGoal(it) }
                .firstOrNull()
        }
    }

    // Save nutrition goals
    fun save(nutritionGoal: NutritionGoal): Int {
        return transaction {
            NutritionGoals.insert {
                it[type] = nutritionGoal.type
                it[proteinGoal] = nutritionGoal.proteinGoal
                it[fibreGoal] = nutritionGoal.fibreGoal
                it[calorieGoal] = nutritionGoal.calorieGoal
                it[carbsGoal] = nutritionGoal.carbsGoal
                it[fatGoal] = nutritionGoal.fatGoal
                it[userId] = nutritionGoal.userId
            } get NutritionGoals.id
        }
    }

    // Delete nutrition goal
    fun delete(id: Int): Int {
        return transaction {
            NutritionGoals.deleteWhere {
                NutritionGoals.id eq id
            }
        }
    }

    // Update nutrition goal
    fun updateNutritionGoal(goalId: Int, nutritionGoalToUpdate: NutritionGoal): Int {
        return transaction {
            NutritionGoals.update({
                NutritionGoals.id eq goalId
            }) {
                it[type] = nutritionGoalToUpdate.type
                it[proteinGoal] = nutritionGoalToUpdate.proteinGoal
                it[fibreGoal] = nutritionGoalToUpdate.fibreGoal
                it[calorieGoal] = nutritionGoalToUpdate.calorieGoal
                it[carbsGoal] = nutritionGoalToUpdate.carbsGoal
                it[fatGoal] = nutritionGoalToUpdate.fatGoal
                it[userId] = nutritionGoalToUpdate.userId
            }
        }
    }
}
