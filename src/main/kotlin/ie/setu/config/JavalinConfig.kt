package ie.setu.config

//import ie.setu.controllers.HealthTrackerController
import ie.setu.controllers.UserController
import ie.setu.controllers.ActivityController
import ie.setu.controllers.FitnessGoalsController
import ie.setu.controllers.HealthRecordController
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create{
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 : Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7002
    }

    fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}") {
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("healthRecords") {
                        get(HealthRecordController::getHealthRecordByUserId)
                        post(HealthRecordController::addHealthRecord)
                    }
                    path("fitnessGoals") {
                        get(FitnessGoalsController::getFitnessGoalsByUserId)
                        post(FitnessGoalsController::addFitnessGoal)
                    }
                    path("activities") {
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteAllActivitiesByUserId)
                    }
                }
                path("/email/{email}") {
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}") {
                    get(ActivityController::getActivityByActivityId)
                    delete(ActivityController::deleteActivity)
                    patch(ActivityController::updateActivity)
                }
            }
            path("/api/healthRecords") {
                get(HealthRecordController::getAllHealthRecords)
                post(HealthRecordController::addHealthRecord)
                path("{health-record-id}") {
                    get(HealthRecordController::getHealthRecordByHealthRecordId)
                    delete(HealthRecordController::deleteHealthRecord)
                    patch(HealthRecordController::updateHealthRecord)
                }
            }
            path("/api/fitnessGoals") {
                get(FitnessGoalsController::getAllFitnessGoals)
                post(FitnessGoalsController::addFitnessGoal)
                path("{fitness-goal-id}") {
                    get(FitnessGoalsController::getFitnessGoalByGoalId)
                    delete(FitnessGoalsController::deleteFitnessGoal)
                    patch(FitnessGoalsController::updateFitnessGoal)
                }
            }
        }
    }
}