package ie.setu.controllers

import ie.setu.domain.MoodEntry
import ie.setu.domain.repository.MoodTrackingDAO
import ie.setu.domain.repository.SleepTrackingDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling mood tracking-related HTTP requests.
 */
object MoodTrackingController {

    private val userDao = UserDAO()
    private val sleepDao =  SleepTrackingDAO()
    private val moodTrackingDAO = MoodTrackingDAO()

    /**
     * Handles the request to get all mood tracking entries.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllMoodTracking(ctx: Context) {
        val moodTrackingEntries = moodTrackingDAO.getAll()
        if (moodTrackingEntries.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(moodTrackingEntries)
    }

    /**
     * Handles the request to get a mood tracking entry by ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getMoodTrackingById(ctx: Context) {
        val moodTrackingEntry = moodTrackingDAO.findById(ctx.pathParam("mood-tracking-id").toInt())
        if (moodTrackingEntry != null) {
            ctx.json(moodTrackingEntry)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get all mood tracking entries for a specific user.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getMoodTrackingByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val moodTrackingEntries = moodTrackingDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (moodTrackingEntries.isNotEmpty()) {
                ctx.json(moodTrackingEntries)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get all mood tracking entries for a specific sleep ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getMoodTrackingBySleepId(ctx: Context) {
        if (sleepDao.findById(ctx.pathParam("sleep-tracking-id").toInt()) != null) {
            val sleepId = ctx.pathParam("sleep-tracking-id").toInt()
            val moodTrackingEntries = moodTrackingDAO.findBySleepId(sleepId)
            if (moodTrackingEntries.isNotEmpty()) {
                ctx.json(moodTrackingEntries)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add a mood tracking entry.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addMoodTracking(ctx: Context) {
        val moodTrackingEntry: MoodEntry = jsonToObject(ctx.body())
        val user = userDao.findById(moodTrackingEntry.userId)
        if (user != null) {
            val entryId = moodTrackingDAO.save(moodTrackingEntry)
            moodTrackingEntry.id = entryId
            ctx.json(moodTrackingEntry)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to update a mood tracking entry.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateMoodTracking(ctx: Context) {
        val moodTrackingEntry: MoodEntry = jsonToObject(ctx.body())
        if (moodTrackingDAO.updateMoodTracking(
                id = ctx.pathParam("mood-tracking-id").toInt(),
                updatedMoodTracking = moodTrackingEntry
            ) != 0
        ) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to delete a mood tracking entry by ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteMoodTracking(ctx: Context) {
        val entryId = ctx.pathParam("mood-tracking-id").toInt()
        val deletedCount = moodTrackingDAO.delete(entryId)
        if (deletedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
