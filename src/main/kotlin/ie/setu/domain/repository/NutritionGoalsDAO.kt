package ie.setu.domain.repository

import ie.setu.domain.NutritionGoal
import ie.setu.domain.db.NutritionGoals
import ie.setu.utils.mapToNutritionGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `NutritionGoals` table in the database.
 * It provides methods to perform CRUD operations on nutrition goals.
 */
class NutritionGoalsDAO {

    /**
     * Get all nutrition goals from the database.
     *
     * @return List of nutrition goals.
     */
    fun getAll(): ArrayList<NutritionGoal> {
        val ngList: ArrayList<NutritionGoal> = arrayListOf()
        transaction {
            NutritionGoals.selectAll().map {
                ngList.add(mapToNutritionGoal(it))
            }
        }
        return ngList
    }

    /**
     * Find nutrition goals by user ID.
     *
     * @param userId The ID of the user to find nutrition goals for.
     * @return List of nutrition goals for the specified user.
     */
    fun findByUserId(userId: Int): List<NutritionGoal> {
        return transaction {
            NutritionGoals
                .select { NutritionGoals.userId eq userId }
                .map { mapToNutritionGoal(it) }
        }
    }

    /**
     * Find nutrition goals by weight goal ID.
     *
     * @param weightGoalId The ID of the weight goal associated with nutrition goals.
     * @return List of nutrition goals associated with the specified weight goal.
     */
    fun findNutritionGoalsByWeightGoalId(weightGoalId: Int): List<NutritionGoal> {
        return transaction {
            NutritionGoals
                .select { NutritionGoals.weightId eq weightGoalId }
                .map { mapToNutritionGoal(it) }
        }
    }

    /**
     * Find nutrition goals by fitness goal ID.
     *
     * @param fitnessGoalId The ID of the fitness goal associated with nutrition goals.
     * @return List of nutrition goals associated with the specified fitness goal.
     */
    fun findByFitnessId(fitnessGoalId: Int): List<NutritionGoal> {
        return transaction {
            NutritionGoals
                .select { NutritionGoals.fitnessId eq fitnessGoalId }
                .map { mapToNutritionGoal(it) }
        }
    }

    /**
     * Find a specific nutrition goal by goal ID.
     *
     * @param id The ID of the nutrition goal to find.
     * @return The found nutrition goal, or null if not found.
     */
    fun findByGoalId(id: Int): NutritionGoal? {
        return transaction {
            NutritionGoals
                .select { NutritionGoals.id eq id }
                .map { mapToNutritionGoal(it) }
                .firstOrNull()
        }
    }

    /**
     * Save nutrition goals to the database.
     *
     * @param nutritionGoal The nutrition goal to save.
     * @return The ID of the saved nutrition goal.
     */
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
                it[fitnessId] = nutritionGoal.fitnessId
                it[weightId] = nutritionGoal.weightId
            } get NutritionGoals.id
        }
    }

    /**
     * Delete nutrition goal by ID.
     *
     * @param id The ID of the nutrition goal to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            NutritionGoals.deleteWhere {
                NutritionGoals.id eq id
            }
        }
    }

    /**
     * Update nutrition goal by ID.
     *
     * @param goalId The ID of the nutrition goal to update.
     * @param nutritionGoalToUpdate The updated nutrition goal information.
     * @return The number of updated rows.
     */
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
                it[fitnessId] = nutritionGoalToUpdate.fitnessId
                it[weightId] = nutritionGoalToUpdate.weightId
            }
        }
    }
}
