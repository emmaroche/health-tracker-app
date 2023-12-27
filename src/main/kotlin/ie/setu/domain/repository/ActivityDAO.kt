package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ActivityDAO {

    // Get all the activities in the database regardless of user ID
    fun getAll(): ArrayList<Activity> {
        val activitiesList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivity(it)) }
        }
        return activitiesList
    }

    // Find a specific activity by activity ID
    fun findByActivityId(id: Int): Activity?{
        return transaction {
            Activities
                .select { Activities.id eq id}
                .map{mapToActivity(it)}
                .firstOrNull()
        }
    }

    // Find all activities for a specific user ID
    fun findByUserId(userId: Int): List<Activity>{
        return transaction {
            Activities
                .select {Activities.userId eq userId}
                .map {mapToActivity(it)}
        }
    }

    // Find all activities for a specific fitness ID
    fun findByFitnessId(fitnessId: Int): List<Activity> {
        return transaction {
            Activities
                .select { Activities.fitnessId eq fitnessId }
                .map { mapToActivity(it) }
        }
    }

    // Save an activity to the database
    fun save(activity: Activity): Int {
        return transaction {
            Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[calories] = activity.calories
                it[started] = activity.started
                it[userId] = activity.userId
                it[fitnessId] = activity.fitnessId
            }
        } get Activities.id
    }

    // Delete all activities associated with a user
    fun deleteByActivityId (activityId: Int): Int{
        return transaction{
            Activities.deleteWhere { Activities.id eq activityId }
        }
    }

    // Delete a specific activity by activity ID
    fun deleteAllByUserId (userId: Int): Int{
        return transaction{
            Activities.deleteWhere { Activities.userId eq userId }
        }
    }

    // Update a specific activity by activity ID
    fun updateByActivityId(activityId: Int, activityToUpdate: Activity) : Int{
        return transaction {
            Activities.update ({
                Activities.id eq activityId}) {
                it[description] = activityToUpdate.description
                it[duration] = activityToUpdate.duration
                it[calories] = activityToUpdate.calories
                it[started] = activityToUpdate.started
                it[userId] = activityToUpdate.userId
                it[fitnessId] = activityToUpdate.fitnessId
            }
        }
    }

}
