//package ie.setu.controllers
//
//import com.fasterxml.jackson.databind.SerializationFeature
//import com.fasterxml.jackson.datatype.joda.JodaModule
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import com.fasterxml.jackson.module.kotlin.readValue
//import ie.setu.domain.Activity
//import ie.setu.domain.User
//import ie.setu.domain.repository.ActivityDAO
//import ie.setu.domain.repository.UserDAO
//import io.javalin.http.Context
//
//object HealthTrackerController {
//
//    val userDao = UserDAO()
//    val activityDAO = ActivityDAO()
//
//    fun getAllUsers(ctx: Context) {
//        ctx.json(userDao.getAll())
//    }
//
//    fun getUserByUserId(ctx: Context) {
//        val user = userDao.findById(ctx.pathParam("user-id").toInt())
//        if (user != null) {
//            ctx.json(user)
//        }
//    }
//
//    fun addUser(ctx: Context) {
//        val mapper = jacksonObjectMapper()
//        val user = mapper.readValue<User>(ctx.body())
//        userDao.save(user)
//        ctx.json(user)
//    }
//
//    fun getUserByEmail(ctx: Context) {
//        val user = userDao.findByEmail(ctx.pathParam("email"))
//        if (user != null) {
//            ctx.json(user)
//        }
//    }
//
//    fun deleteUser(ctx: Context) {
//        userDao.delete(ctx.pathParam("user-id").toInt())
//    }
//
//    fun updateUser(ctx: Context) {
//        val mapper = jacksonObjectMapper()
//        val userUpdates = mapper.readValue<User>(ctx.body())
//        userDao.update(
//            id = ctx.pathParam("user-id").toInt(),
//            user = userUpdates
//        )
//    }
//
//    //--------------------------------------------------------------
//    // ActivityDAOI specifics
//    //-------------------------------------------------------------
//
//    fun getAllActivities(ctx: Context) {
//        //mapper handles the deserialization of Joda date into a String.
//        val mapper = jacksonObjectMapper()
//            .registerModule(JodaModule())
//            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//        ctx.json(mapper.writeValueAsString(activityDAO.getAll()))
//    }
//
//    fun getActivitiesByUserId(ctx: Context) {
//        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
//            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
//            if (activities.isNotEmpty()) {
//                //mapper handles the deserialization of Joda date into a String.
//                val mapper = jacksonObjectMapper()
//                    .registerModule(JodaModule())
//                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//                ctx.json(mapper.writeValueAsString(activities))
//            }
//        }
//    }
//
//    fun addActivity(ctx: Context) {
//        //mapper handles the serialisation of Joda date into a String.
//        val mapper = jacksonObjectMapper()
//            .registerModule(JodaModule())
//            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//        val activity = mapper.readValue<Activity>(ctx.body())
//        activityDAO.save(activity)
//        ctx.json(activity)
//    }
//
//    // Delete all activities associated with a user
//    fun deleteAllActivitiesByUserId(ctx: Context) {
//        val userId = ctx.pathParam("user-id").toInt()
//        activityDAO.deleteAllByUserId(userId)
//    }
//
//    // Delete a specific activity by activity id
//    fun deleteActivity(ctx: Context) {
//        val activityId = ctx.pathParam("activity-id").toInt()
//        activityDAO.deleteByActivityId(activityId)
//    }
//
//    // Update a specific activity by activity id
//    fun updateActivity(ctx: Context) {
//        val mapper = jacksonObjectMapper()
//        val activityUpdates = mapper.readValue<Activity>(ctx.body())
//        val activityId = ctx.pathParam("activity-id").toInt()
//        activityDAO.updateByActivityId(activityId, activityUpdates)
//    }
//
//    // Get activities by activity id
//    fun getActivityByActivityId(ctx: Context) {
//        val activityId = ctx.pathParam("activity-id").toInt()
//        val activity = activityDAO.findByActivityId(activityId)
//        if (activity != null) {
//            ctx.json(activity)
//        }
//    }
//
//}
//
//
