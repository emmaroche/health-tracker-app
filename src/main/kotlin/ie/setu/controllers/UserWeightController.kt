package ie.setu.controllers

import ie.setu.domain.CurrentWeight
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.UserWeightDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object UserWeightController {

    private val userDao = UserDAO()
    private val userWeightDAO = UserWeightDAO()

    // Get all user weights
    fun getAllUserWeights(ctx: Context) {
        val userWeights = userWeightDAO.getAll()
        if (userWeights.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(userWeights)
    }

    // Get user weight by weight ID
    fun getUserWeightById(ctx: Context) {
        val userWeight = userWeightDAO.findByWeightId(ctx.pathParam("user-weight-id").toInt())
        if (userWeight != null) {
            ctx.status(200)
            ctx.json(userWeight)
        } else {
            ctx.status(404)
        }
    }

    // Get user weights by user ID
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

    // Add user weight
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

    // Update user weight
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

    // Delete user weight
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
