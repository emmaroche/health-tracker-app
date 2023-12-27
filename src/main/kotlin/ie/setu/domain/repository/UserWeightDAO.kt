package ie.setu.domain.repository

import ie.setu.domain.CurrentWeight
import ie.setu.domain.db.UserWeight
import ie.setu.utils.mapToCurrentWeight
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `UserWeight` table in the database.
 * It provides methods to perform CRUD operations on user weight goal data.
 */
class UserWeightDAO {

    /**
     * Get all user weights from the database.
     *
     * @return List of user weights.
     */
    fun getAll(): List<CurrentWeight> {
        return transaction {
            UserWeight.selectAll().map { mapToCurrentWeight(it) }
        }
    }

    /**
     * Find user weights by user ID.
     *
     * @param userId The ID of the user to find weights for.
     * @return List of user weights for the specified user.
     */
    fun findByUserId(userId: Int): List<CurrentWeight> {
        return transaction {
            UserWeight
                .select { UserWeight.userId eq userId }
                .map { mapToCurrentWeight(it) }
        }
    }

    /**
     * Find a specific user weight by weight ID.
     *
     * @param id The ID of the user weight to find.
     * @return The found user weight, or null if not found.
     */
    fun findByWeightId(id: Int): CurrentWeight? {
        return transaction {
            UserWeight
                .select { UserWeight.id eq id }
                .map { mapToCurrentWeight(it) }
                .firstOrNull()
        }
    }

    /**
     * Save a user weight to the database.
     *
     * @param currentWeight The user weight to save.
     * @return The ID of the saved user weight.
     */
    fun save(currentWeight: CurrentWeight): Int {
        return transaction {
            UserWeight.insert {
                it[this.currentWeight] = currentWeight.currentWeight
                it[this.currentWeightTimestamp] = currentWeight.currentWeightTimestamp
                it[this.wgId] = currentWeight.weightGoalId
                it[this.userId] = currentWeight.userId
            } get UserWeight.id
        }
    }

    /**
     * Delete a user weight by ID.
     *
     * @param id The ID of the user weight to delete.
     * @return The number of deleted rows.
     */
    fun delete(id: Int): Int {
        return transaction {
            UserWeight.deleteWhere {
                UserWeight.id eq id
            }
        }
    }

    /**
     * Update a user weight by weight ID.
     *
     * @param weightId The ID of the user weight to update.
     * @param currentWeightToUpdate The updated user weight information.
     * @return The number of updated rows.
     */
    fun updateCurrentWeight(weightId: Int, currentWeightToUpdate: CurrentWeight): Int {
        return transaction {
            UserWeight.update({
                UserWeight.id eq weightId
            }) {
                it[this.currentWeight] = currentWeightToUpdate.currentWeight
                it[this.currentWeightTimestamp] = currentWeightToUpdate.currentWeightTimestamp
                it[this.wgId] = currentWeightToUpdate.weightGoalId
                it[this.userId] = currentWeightToUpdate.userId
            }
        }
    }
}
