package ie.setu.controllers

import ie.setu.domain.CurrentWeight
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.UserWeightDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling user weight-related HTTP requests.
 */
object UserWeightController {

    private val userDao = UserDAO()
    private val userWeightDAO = UserWeightDAO()

    /**
     * Handles the request to get all user weights.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllUserWeights(ctx: Context) {
        val userWeights = userWeightDAO.getAll()
        if (userWeights.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(userWeights)
    }

    /**
     * Handles the request to get user weight by weight ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getUserWeightById(ctx: Context) {
        val userWeight = userWeightDAO.findByWeightId(ctx.pathParam("user-weight-id").toInt())
        if (userWeight != null) {
            ctx.status(200)
            ctx.json(userWeight)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get user weights by user ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getUserWeightsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val userWeights = userWeightDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (userWeights.isNotEmpty()) {
                ctx.json(userWeights)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add user weight.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addUserWeight(ctx: Context) {
        val userWeight: CurrentWeight = jsonToObject(ctx.body())
        val user = userDao.findById(userWeight.userId)
        if (user != null) {
            val weightId = userWeightDAO.save(userWeight)
            userWeight.id = weightId
            ctx.json(userWeight)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to update user weight.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateUserWeight(ctx: Context) {
        val userWeight: CurrentWeight = jsonToObject(ctx.body())
        if (userWeightDAO.updateCurrentWeight(
                weightId = ctx.pathParam("user-weight-id").toInt(),
                currentWeightToUpdate = userWeight
            ) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to delete user weight.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteUserWeight(ctx: Context) {
        val weightId = ctx.pathParam("user-weight-id").toInt()
        val deletedCount = userWeightDAO.delete(weightId)
        if (deletedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
