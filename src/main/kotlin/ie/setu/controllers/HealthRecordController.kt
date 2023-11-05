package ie.setu.controllers

import ie.setu.domain.HealthRecord
import ie.setu.domain.repository.HealthRecordDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object HealthRecordController {

    private val userDao = UserDAO()
    private val healthRecordDAO = HealthRecordDAO()

    // Get all health records
    fun getAllHealthRecords(ctx: Context) {
        val healthRec = healthRecordDAO.getAll()
        if (healthRec.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(healthRec)
    }

    // Get health records by health records ID
    fun getHealthRecordByHealthRecordId(ctx: Context) {
        val hr = healthRecordDAO.findByHealthRecordId((ctx.pathParam("health-record-id").toInt()))
        if (hr != null) {
            ctx.json(hr)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    // Get health records by user ID
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

    // Add a new health record
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

    // Update a health record
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

    // Delete a health record by health record ID
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
