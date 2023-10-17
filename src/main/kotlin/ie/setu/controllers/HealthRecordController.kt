package ie.setu.controllers

import ie.setu.domain.Activity
import ie.setu.domain.HealthRecord
import ie.setu.domain.repository.HealthRecordDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object HealthRecordController {
    private val userDao = UserDAO()
    private val healthRecordDAO = HealthRecordDAO()

    fun getAllHealthRecords(ctx: Context) {
        val healthRecords = healthRecordDAO.getAll()
        ctx.json(healthRecords)
    }

    fun getHealthRecordByUserId(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        val healthRecord = healthRecordDAO.findByUserId(userId)
        if (healthRecord != null) {
            ctx.json(healthRecord)
        } else {
            ctx.status(404)
        }
    }

    fun addHealthRecord(ctx: Context) {
        val healthRecord: HealthRecord = jsonToObject(ctx.body())
        val user = userDao.findById(healthRecord.userId)
        if (user != null) {
            val healthRecordId = healthRecordDAO.saveHealthRecord(healthRecord)
            healthRecord.id = healthRecordId
            ctx.json(healthRecord)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun updateHealthRecord(ctx: Context) {
        val healthRecordId = ctx.pathParam("health-record-id").toInt()
        val foundHealthRecord: HealthRecord = jsonToObject(ctx.body())
        val updatedCount = healthRecordDAO.updateHealthRecord(healthRecordId, foundHealthRecord)
        if (updatedCount != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

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
