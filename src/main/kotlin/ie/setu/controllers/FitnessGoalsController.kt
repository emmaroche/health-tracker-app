package ie.setu.controllers

import ie.setu.domain.FitnessGoal
import ie.setu.domain.repository.FitnessGoalsDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object FitnessGoalsController {

    private val userDao = UserDAO()
    private val fitnessGoalsDAO = FitnessGoalsDAO()

    fun getAllFitnessGoals(ctx: Context) {
        val fitnessGoals = fitnessGoalsDAO.getAll()
        if (fitnessGoals.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(fitnessGoals)
    }

    fun getFitnessGoalByGoalId(ctx: Context) {
        val fitnessGoal = fitnessGoalsDAO.findByGoalId(ctx.pathParam("fitness-goal-id").toInt())
        if (fitnessGoal != null) {
            ctx.json(fitnessGoal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

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

    fun addFitnessGoal(ctx: Context) {
        val fitnessGoal: FitnessGoal = jsonToObject(ctx.body())
        val user = userDao.findById(fitnessGoal.userId)
        if (user != null) {
            val goalId = fitnessGoalsDAO.save(fitnessGoal)
            fitnessGoal.id = goalId
            ctx.json(fitnessGoal)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

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
