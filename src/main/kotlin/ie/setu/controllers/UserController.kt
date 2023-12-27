package ie.setu.controllers

import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling user-related HTTP requests.
 */
object UserController {

    private val userDao = UserDAO()

    /**
     * Handles the request to get all users.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(users)
    }

    /**
     * Handles the request to get a user by user ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add a user.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addUser(ctx: Context) {
        val user: User = jsonToObject(ctx.body())
        val userId = userDao.save(user)
        user.id = userId
        ctx.json(user)
        ctx.status(201)
    }

    /**
     * Handles the request to get a user by email.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to delete a user.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteUser(ctx: Context) {
        if (userDao.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    /**
     * Handles the request to update a user.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateUser(ctx: Context) {
        val foundUser: User = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), user = foundUser)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}
