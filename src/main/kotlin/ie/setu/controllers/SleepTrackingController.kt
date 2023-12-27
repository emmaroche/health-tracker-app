package ie.setu.controllers

import ie.setu.domain.SleepEntry
import ie.setu.domain.repository.SleepTrackingDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling sleep tracking-related HTTP requests.
 */
object SleepTrackingController {

    private val userDao = UserDAO()
    private val sleepTrackingDAO = SleepTrackingDAO()

    /**
     * Handles the request to get all sleep tracking entries.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllSleepTracking(ctx: Context) {
        val sleepTrackingEntries = sleepTrackingDAO.getAll()
        if (sleepTrackingEntries.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(sleepTrackingEntries)
    }

    /**
     * Handles the request to get a sleep tracking entry by ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getSleepTrackingById(ctx: Context) {
        val sleepTrackingEntry = sleepTrackingDAO.findById(ctx.pathParam("sleep-tracking-id").toInt())
        if (sleepTrackingEntry != null) {
            ctx.json(sleepTrackingEntry)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get all sleep tracking entries for a specific user.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getSleepTrackingByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val sleepTrackingEntries = sleepTrackingDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (sleepTrackingEntries.isNotEmpty()) {
                ctx.json(sleepTrackingEntries)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add a sleep tracking entry.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addSleepTracking(ctx: Context) {
        val sleepTrackingEntry: SleepEntry = jsonToObject(ctx.body())
        val user = userDao.findById(sleepTrackingEntry.userId)
        if (user != null) {
            val entryId = sleepTrackingDAO.save(sleepTrackingEntry)
            sleepTrackingEntry.id = entryId
            ctx.json(sleepTrackingEntry)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to update a sleep tracking entry.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateSleepTracking(ctx: Context) {
        val sleepTrackingEntry: SleepEntry = jsonToObject(ctx.body())
        if (sleepTrackingDAO.updateSleepTracking(
                id = ctx.pathParam("sleep-tracking-id").toInt(),
                updatedSleepTracking = sleepTrackingEntry
            ) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to delete a sleep tracking entry by ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteSleepTracking(ctx: Context) {
        val entryId = ctx.pathParam("sleep-tracking-id").toInt()
        val deletedCount = sleepTrackingDAO.delete(entryId)
        if (deletedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
