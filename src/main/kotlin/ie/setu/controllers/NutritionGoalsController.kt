package ie.setu.controllers

import ie.setu.domain.NutritionGoal
import ie.setu.domain.repository.FitnessGoalsDAO
import ie.setu.domain.repository.NutritionGoalsDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.WeightGoalsDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling nutrition goal-related HTTP requests.
 */
object NutritionGoalsController {

    private val userDao = UserDAO()
    private val fitnessDao = FitnessGoalsDAO()
    private val weightGoalDao = WeightGoalsDAO()
    private val nutritionGoalsDAO = NutritionGoalsDAO()

    /**
     * Handles the request to get all nutrition goals.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllNutritionGoals(ctx: Context) {
        val nutritionGoals = nutritionGoalsDAO.getAll()
        if (nutritionGoals.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(nutritionGoals)
    }

    /**
     * Handles the request to get a nutrition goal by goal ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getNutritionGoalByGoalId(ctx: Context) {
        val nutritionGoal = nutritionGoalsDAO.findByGoalId(ctx.pathParam("nutrition-goal-id").toInt())
        if (nutritionGoal != null) {
            ctx.json(nutritionGoal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get all nutrition goals for a specific user.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getNutritionGoalsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val nutritionGoals = nutritionGoalsDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (nutritionGoals.isNotEmpty()) {
                ctx.json(nutritionGoals)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get all nutrition goals for a specific weight goal ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getNutritionGoalsByWeightGoalId(ctx: Context) {
        if (weightGoalDao.findByGoalId(ctx.pathParam("weight-goal-id").toInt()) != null) {
            val nutritionGoals = nutritionGoalsDAO.findNutritionGoalsByWeightGoalId(ctx.pathParam("weight-goal-id").toInt())
            if (nutritionGoals.isNotEmpty()) {
                ctx.json(nutritionGoals)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get all nutrition goals for a specific fitness goal ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getNutritionGoalsByFitnessId(ctx: Context) {
        if (fitnessDao.findByGoalId(ctx.pathParam("fitness-goal-id").toInt()) != null) {
            val nutritionGoals = nutritionGoalsDAO.findByFitnessId(ctx.pathParam("fitness-goal-id").toInt())
            if (nutritionGoals.isNotEmpty()) {
                ctx.json(nutritionGoals)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get a nutrition goal by type.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getNutritionGoalByType(ctx: Context) {
        val nutritionGoal = nutritionGoalsDAO.findByType(ctx.pathParam("type"))
        if (nutritionGoal != null) {
            ctx.json(nutritionGoal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add a nutrition goal.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addNutritionGoal(ctx: Context) {
        val nutritionGoal: NutritionGoal = jsonToObject(ctx.body())
        val user = userDao.findById(nutritionGoal.userId)
        if (user != null) {
            val goalId = nutritionGoalsDAO.save(nutritionGoal)
            nutritionGoal.id = goalId
            ctx.json(nutritionGoal)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to update a nutrition goal.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateNutritionGoal(ctx: Context) {
        val nutritionGoal: NutritionGoal = jsonToObject(ctx.body())
        if (nutritionGoalsDAO.updateNutritionGoal(
                goalId = ctx.pathParam("nutrition-goal-id").toInt(),
                nutritionGoalToUpdate = nutritionGoal
            ) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to delete a nutrition goal by goal ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteNutritionGoal(ctx: Context) {
        val goalId = ctx.pathParam("nutrition-goal-id").toInt()
        val deletedCount = nutritionGoalsDAO.delete(goalId)
        if (deletedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
