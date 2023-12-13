package ie.setu.domain.repository

import ie.setu.domain.CurrentWeight
import ie.setu.domain.db.UserWeight
import ie.setu.domain.db.WeightGoals
import ie.setu.utils.mapToCurrentWeight
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserWeightDAO {

    // Get all user weights
    fun getAll(): List<CurrentWeight> {
        return transaction {
            UserWeight.selectAll().map { mapToCurrentWeight(it) }
        }
    }

    // Find user weights by user ID
    fun findByUserId(userId: Int): List<CurrentWeight> {
        return transaction {
            UserWeight
                .select { UserWeight.userId eq userId }
                .map { mapToCurrentWeight(it) }
        }
    }

    // Find a specific user weight by weight ID
    fun findByWeightId(id: Int): CurrentWeight? {
        return transaction {
            UserWeight
                .select { UserWeight.id eq id }
                .map { mapToCurrentWeight(it) }
                .firstOrNull()
        }
    }

    // Save user weight
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

    // Delete user weight
    fun delete(id: Int): Int {
        return transaction {
            UserWeight.deleteWhere { UserWeight.id eq id }
        }
    }

    // Update user weight
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
