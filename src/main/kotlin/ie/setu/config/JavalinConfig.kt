package ie.setu.config

//import ie.setu.controllers.HealthTrackerController
import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create{
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueAppName = "app" // only required for Vue 3, is defined in layout.html
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
                    path("weightGoals") {
                        get(WeightGoalsController::getWeightGoalsByUserId)
                        post(WeightGoalsController::addWeightGoal)
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
            path("/api/weightGoals") {
                get(WeightGoalsController::getAllWeightGoals)
                post(WeightGoalsController::addWeightGoal)
                path("{weight-goal-id}") {
                    get(WeightGoalsController::getWeightGoalByGoalId)
                    delete(WeightGoalsController::deleteWeightGoal)
                    patch(WeightGoalsController::updateWeightGoal)
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
                path("type/{type}") {
                    get(FitnessGoalsController::getFitnessGoalByType)
                }

            }
            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside the VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/fitnessGoals", VueComponent("<fitness-goals-overview></fitness-goals-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))
            get("/fitnessGoals/{fitness-goal-id}", VueComponent("<fitness-goals-profile></fitness-goals-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
        }
    }
}