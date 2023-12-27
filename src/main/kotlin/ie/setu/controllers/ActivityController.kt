package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FitnessGoalsDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling activities-related HTTP requests.
 */
object ActivityController {

    private val userDao = UserDAO()
    private val fitnessDao = FitnessGoalsDAO()
    private val activityDAO = ActivityDAO()

    /**
     * Handles the request to get all activities.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllActivities(ctx: Context) {
        val activities = activityDAO.getAll()
        if (activities.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(activities)
    }

    /**
     * Handles the request to get activities by user ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                ctx.json(activities)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get activities by fitness ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getActivitiesByFitnessId(ctx: Context) {
        if (fitnessDao.findByGoalId(ctx.pathParam("fitness-goal-id").toInt()) != null) {
            val activities = activityDAO.findByFitnessId(ctx.pathParam("fitness-goal-id").toInt())
            if (activities.isNotEmpty()) {
                ctx.json(activities)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add a new activity.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addActivity(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        val userId = userDao.findById(activity.userId)
        if (userId != null) {
            val activityId = activityDAO.save(activity)
            activity.id = activityId
            ctx.json(activity)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to delete all activities associated with a user.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteAllActivitiesByUserId(ctx: Context) {
        if (activityDAO.deleteAllByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    /**
     * Handles the request to delete a specific activity by activity ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteActivity(ctx: Context) {
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    /**
     * Handles the request to update a specific activity by activity ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateActivity(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        if (activityDAO.updateByActivityId(
                activityId = ctx.pathParam("activity-id").toInt(),
                activityToUpdate = activity
            ) != 0
        )
            ctx.status(204)
        else
            ctx.status(404)
    }

    /**
     * Handles the request to update a specific activity by activity ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getActivityByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null) {
            ctx.json(activity)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }
}