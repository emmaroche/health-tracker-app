package ie.setu.controllers

import ie.setu.domain.WeightGoal
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.WeightGoalsDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling weight goal-related HTTP requests.
 */
object WeightGoalsController {

    private val userDao = UserDAO()
    private val actDAO = ActivityDAO()
    private val weightGoalsDAO = WeightGoalsDAO()

    /**
     * Handles the request to get all weight goals.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllWeightGoals(ctx: Context) {
        val weightGoals = weightGoalsDAO.getAll()
        if (weightGoals.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(weightGoals)
    }

    /**
     * Handles the request to get weight goal by goal ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getWeightGoalByGoalId(ctx: Context) {
        val weightGoal = weightGoalsDAO.findByGoalId(ctx.pathParam("weight-goal-id").toInt())
        if (weightGoal != null) {
            ctx.json(weightGoal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get weight goals by user ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getWeightGoalsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val weightGoals = weightGoalsDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (weightGoals.isNotEmpty()) {
                ctx.json(weightGoals)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get weight goals by activity ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getWeightGoalsByActivityId(ctx: Context) {
        if (actDAO.findByActivityId(ctx.pathParam("activity-id").toInt()) != null) {
            val weightGoals = weightGoalsDAO.findByActivityId(ctx.pathParam("activity-id").toInt())
            if (weightGoals.isNotEmpty()) {
                ctx.json(weightGoals)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add weight goal.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addWeightGoal(ctx: Context) {
        val weightGoal: WeightGoal = jsonToObject(ctx.body())
        val user = userDao.findById(weightGoal.userId)
        if (user != null) {
            val goalId = weightGoalsDAO.save(weightGoal)
            weightGoal.id = goalId
            ctx.json(weightGoal)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to update weight goal.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateWeightGoal(ctx: Context) {
        val weightGoal: WeightGoal = jsonToObject(ctx.body())
        if (weightGoalsDAO.updateWeightGoal(
                goalId = ctx.pathParam("weight-goal-id").toInt(),
                weightGoalToUpdate = weightGoal
            ) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to delete weight goal.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteWeightGoal(ctx: Context) {
        val goalId = ctx.pathParam("weight-goal-id").toInt()
        val deletedCount = weightGoalsDAO.delete(goalId)
        if (deletedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
