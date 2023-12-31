package ie.setu.controllers

import ie.setu.domain.HealthRecord
import ie.setu.domain.repository.HealthRecordDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

/**
 * Controller for handling health records-related HTTP requests.
 */
object HealthRecordController {

    private val userDao = UserDAO()
    private val healthRecordDAO = HealthRecordDAO()

    /**
     * Handles the request to get all health records.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getAllHealthRecords(ctx: Context) {
        val healthRec = healthRecordDAO.getAll()
        if (healthRec.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(healthRec)
    }

    /**
     * Handles the request to get a health record by health record ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getHealthRecordByHealthRecordId(ctx: Context) {
        val hr = healthRecordDAO.findByHealthRecordId((ctx.pathParam("health-record-id").toInt()))
        if (hr != null) {
            ctx.json(hr)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to get health records by user ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun getHealthRecordByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val healthRec = healthRecordDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (healthRec.isNotEmpty()) {
                ctx.json(healthRec)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to add a new health record.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun addHealthRecord(ctx: Context) {
        val healthRecord: HealthRecord = jsonToObject(ctx.body())
        val user = userDao.findById(healthRecord.userId)
        if (user != null) {
            val healthRecordId = healthRecordDAO.save(healthRecord)
            healthRecord.id = healthRecordId
            ctx.json(healthRecord)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Handles the request to update a health record.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun updateHealthRecord(ctx: Context) {
        val hr: HealthRecord = jsonToObject(ctx.body())
        if (healthRecordDAO.updateHealthRecord(
                healthId = ctx.pathParam("health-record-id").toInt(),
                healthRecordToUpdate = hr
            ) != 0
        )
            ctx.status(204)
        else
            ctx.status(404)
    }

    /**
     * Handles the request to delete a health record by health record ID.
     *
     * @param ctx The Javalin [Context] object representing the HTTP context.
     */
    fun deleteHealthRecord(ctx: Context) {
        val healthRecordId = ctx.pathParam("health-record-id").toInt()
        val deletedCount = healthRecordDAO.delete(healthRecordId)
        if (deletedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
