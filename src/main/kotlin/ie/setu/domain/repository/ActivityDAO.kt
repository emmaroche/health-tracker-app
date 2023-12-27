package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Data Access Object (DAO) for interacting with the `Activities` table in the database.
 * It provides methods to perform CRUD operations on activity records.
 */
class ActivityDAO {

    /**
     * Get all activities in the database regardless of user ID.
     *
     * @return List of activities.
     */
    fun getAll(): ArrayList<Activity> {
        val activitiesList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivity(it))
            }
        }
        return activitiesList
    }

    /**
     * Find a specific activity by activity ID.
     *
     * @param id The ID of the activity to find.
     * @return The found activity, or null if not found.
     */
    fun findByActivityId(id: Int): Activity? {
        return transaction {
            Activities
                .select { Activities.id eq id }
                .map { mapToActivity(it) }
                .firstOrNull()
        }
    }

    /**
     * Find all activities for a specific user ID.
     *
     * @param userId The ID of the user to find activities for.
     * @return List of activities for the specified user.
     */
    fun findByUserId(userId: Int): List<Activity> {
        return transaction {
            Activities
                .select { Activities.userId eq userId }
                .map { mapToActivity(it) }
        }
    }

    /**
     * Find all activities for a specific fitness ID.
     *
     * @param fitnessId The ID of the fitness goal to find activities for.
     * @return List of activities for the specified fitness goal.
     */
    fun findByFitnessId(fitnessId: Int): List<Activity> {
        return transaction {
            Activities
                .select { Activities.fitnessId eq fitnessId }
                .map { mapToActivity(it) }
        }
    }

    /**
     * Save an activity to the database.
     *
     * @param activity The activity to save.
     * @return The ID of the saved activity.
     */
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

    /**
     * Delete a specific activity by activity ID.
     *
     * @param activityId The ID of the activity to delete.
     * @return The number of deleted rows.
     */
    fun deleteByActivityId(activityId: Int): Int {
        return transaction {
            Activities.deleteWhere { Activities.id eq activityId }
        }
    }

    /**
     * Delete all activities associated with a user.
     *
     * @param userId The ID of the user to delete activities for.
     * @return The number of deleted rows.
     */
    fun deleteAllByUserId(userId: Int): Int {
        return transaction {
            Activities.deleteWhere { Activities.userId eq userId }
        }
    }

    /**
     * Update a specific activity by activity ID.
     *
     * @param activityId The ID of the activity to update.
     * @param activityToUpdate The updated activity information.
     * @return The number of updated rows.
     */
    fun updateByActivityId(activityId: Int, activityToUpdate: Activity): Int {
        return transaction {
            Activities.update({
                Activities.id eq activityId
            }) {
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
