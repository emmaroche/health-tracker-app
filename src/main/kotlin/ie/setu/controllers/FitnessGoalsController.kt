package ie.setu.controllers

import ie.setu.domain.FitnessGoal
import ie.setu.domain.repository.FitnessGoalsDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object FitnessGoalsController {

    private val userDao = UserDAO()
    private val fitnessGoalsDAO = FitnessGoalsDAO()

    // Get all fitness goals
    fun getAllFitnessGoals(ctx: Context) {
        val fitnessGoals = fitnessGoalsDAO.getAll()
        if (fitnessGoals.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(fitnessGoals)
    }

    // Get fitness goals by goal ID
    fun getFitnessGoalByGoalId(ctx: Context) {
        val fitnessGoal = fitnessGoalsDAO.findByGoalId(ctx.pathParam("fitness-goal-id").toInt())
        if (fitnessGoal != null) {
            ctx.json(fitnessGoal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    // Get all fitness goals for a specific user
    fun getFitnessGoalsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val fitnessGoals = fitnessGoalsDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (fitnessGoals.isNotEmpty()) {
                ctx.json(fitnessGoals)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    // Get a fitness goal by type
    fun getFitnessGoalByType(ctx: Context) {
        val fitnessGoal = fitnessGoalsDAO.findByType(ctx.pathParam("type"))
        if (fitnessGoal != null) {
            ctx.json(fitnessGoal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    // Add a fitness goal
    fun addFitnessGoal(ctx: Context) {
        val fitnessGoals: FitnessGoal = jsonToObject(ctx.body())
        val user = userDao.findById(fitnessGoals.userId)
        if (user != null) {
            val goalId = fitnessGoalsDAO.save(fitnessGoals)
            fitnessGoals.id = goalId
            ctx.json(fitnessGoals)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    // Update a fitness goal
    fun updateFitnessGoal(ctx: Context) {
        val fitnessGoal: FitnessGoal = jsonToObject(ctx.body())
        if (fitnessGoalsDAO.updateFitnessGoal(
                goalId = ctx.pathParam("fitness-goal-id").toInt(),
                fitnessGoalToUpdate = fitnessGoal
            ) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    // Delete a fitness goal by goal ID
    fun deleteFitnessGoal(ctx: Context) {
        val goalId = ctx.pathParam("fitness-goal-id").toInt()
        val deletedCount = fitnessGoalsDAO.delete(goalId)
        if (deletedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
