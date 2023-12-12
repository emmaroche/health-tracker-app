package ie.setu.controllers

import ie.setu.domain.SleepEntry
import ie.setu.domain.repository.SleepTrackingDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object SleepTrackingController {

    private val userDao = UserDAO()
    private val sleepTrackingDAO = SleepTrackingDAO()

    // Get all sleep tracking entries
    fun getAllSleepTracking(ctx: Context) {
        val sleepTrackingEntries = sleepTrackingDAO.getAll()
        if (sleepTrackingEntries.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(sleepTrackingEntries)
    }

    // Get sleep tracking entry by ID
    fun getSleepTrackingById(ctx: Context) {
        val sleepTrackingEntry = sleepTrackingDAO.findById(ctx.pathParam("sleep-tracking-id").toInt())
        if (sleepTrackingEntry != null) {
            ctx.json(sleepTrackingEntry)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    // Get all sleep tracking entries for a specific user
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

    // Add a sleep tracking entry
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

    // Update a sleep tracking entry
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

    // Delete a sleep tracking entry by ID
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
