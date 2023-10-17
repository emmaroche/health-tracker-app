package ie.setu.controllers

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
        if (healthRecords.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(healthRecords)
    }

    fun getHealthRecordByUserId(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        val healthRecord = healthRecordDAO.findByUserId(userId)
        if (healthRecord != null) {
            ctx.json(healthRecord)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addHealthRecord(ctx: Context) {
        val healthRecord: HealthRecord = jsonToObject(ctx.body())
        val userId = ActivityController.userDao.findById(healthRecord.userId)
        if (userId != null) {
            val healthRecordId = healthRecordDAO.saveHealthRecord(healthRecord)
            healthRecord.id = healthRecordId
            ctx.json(healthRecord)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun updateHealthRecord(ctx: Context) {
        val foundHealthRecord: HealthRecord = jsonToObject(ctx.body())
        if (healthRecordDAO.updateHealthRecord(id = ctx.pathParam("health-record-id").toInt(), healthRecord = foundHealthRecord) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun deleteHealthRecord(ctx: Context) {
        if (healthRecordDAO.delete(ctx.pathParam("health-record-id").toInt()) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}
