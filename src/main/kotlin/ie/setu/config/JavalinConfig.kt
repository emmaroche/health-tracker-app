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
                    path("nutritionGoals") {
                        get(NutritionGoalsController::getNutritionGoalsByUserId)
                        post(NutritionGoalsController::addNutritionGoal)
                    }
                    path("activities") {
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteAllActivitiesByUserId)
                    }
                    path("sleepTracking") {
                        get(SleepTrackingController::getSleepTrackingByUserId)
                        post(SleepTrackingController::addSleepTracking)
                    }
                    path("moodTracking") {
                        get(MoodTrackingController::getMoodTrackingByUserId)
                        post(MoodTrackingController::addMoodTracking)
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
                    path("weightGoals") {
                        get(WeightGoalsController::getWeightGoalsByActivityId)
                    }
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
                    path("nutritionGoals") {
                        get(NutritionGoalsController::getNutritionGoalsByWeightGoalId)
                    }
                    path("userWeight") {
                        get(UserWeightController::getAllUserWeights)
                        post(UserWeightController::addUserWeight)
                        patch(UserWeightController::updateUserWeight)
                    }
                }
            }
            path("/api/fitnessGoals") {
                get(FitnessGoalsController::getAllFitnessGoals)
                post(FitnessGoalsController::addFitnessGoal)
                path("{fitness-goal-id}") {
                    get(FitnessGoalsController::getFitnessGoalByGoalId)
                    delete(FitnessGoalsController::deleteFitnessGoal)
                    patch(FitnessGoalsController::updateFitnessGoal)
                    path("activities") {
                        get(ActivityController::getActivitiesByFitnessId)
                    }
                    path("nutritionGoals") {
                        get(NutritionGoalsController::getNutritionGoalsByFitnessId)
                    }
                }
                path("type/{type}") {
                    get(FitnessGoalsController::getFitnessGoalByType)
                }

            }
            path("/api/nutritionGoals") {
                get(NutritionGoalsController::getAllNutritionGoals)
                post(NutritionGoalsController::addNutritionGoal)
                path("{nutrition-goal-id}") {
                    get(NutritionGoalsController::getNutritionGoalByGoalId)
                    delete(NutritionGoalsController::deleteNutritionGoal)
                    patch(NutritionGoalsController::updateNutritionGoal)
                }
            }
            path("/api/sleepTracking") {
                get(SleepTrackingController::getAllSleepTracking)
                post(SleepTrackingController::addSleepTracking)
                path("{sleep-tracking-id}") {
                    get(SleepTrackingController::getSleepTrackingById)
                    delete(SleepTrackingController::deleteSleepTracking)
                    patch(SleepTrackingController::updateSleepTracking)
                    path("moodTracking") {
                        get(MoodTrackingController::getMoodTrackingBySleepId)
                    }
                }
            }
            path("/api/moodTracking") {
                get(MoodTrackingController::getAllMoodTracking)
                post(MoodTrackingController::addMoodTracking)
                path("{mood-tracking-id}") {
                    get(MoodTrackingController::getMoodTrackingById)
                    delete(MoodTrackingController::deleteMoodTracking)
                    patch(MoodTrackingController::updateMoodTracking)
                }
            }
            path("/api/userWeight") {
                get(UserWeightController::getAllUserWeights)
                post(UserWeightController::addUserWeight)
                path("{user-weight-id}") {
                    get(UserWeightController::getUserWeightById)
                    delete(UserWeightController::deleteUserWeight)
                    patch(UserWeightController::updateUserWeight)
                }
            }
            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside the VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/fitnessGoals", VueComponent("<fitness-goals-overview></fitness-goals-overview>"))
            get("/weightGoals", VueComponent("<weight-goals-overview></weight-goals-overview>"))
            get("/nutritionGoals", VueComponent("<nutrition-goals-overview></nutrition-goals-overview>"))
            get("/healthRecords", VueComponent("<health-record-overview></health-record-overview>"))
            get("/sleepTracking", VueComponent("<sleep-tracking-overview></sleep-tracking-overview>"))
            get("/moodTracking", VueComponent("<mood-tracking-overview></mood-tracking-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))
            get("/fitnessGoals/{fitness-goal-id}", VueComponent("<fitness-goals-profile></fitness-goals-profile>"))
            get("/weightGoals/{weight-goal-id}", VueComponent("<weight-goals-profile></weight-goals-profile>"))
            get("/nutritionGoals/{nutrition-goal-id}", VueComponent("<nutrition-goals-profile></nutrition-goals-profile>"))
            get("/healthRecords/{health-record-id}", VueComponent("<health-record-profile></health-record-profile>"))
            get("/sleepTracking/{sleep-tracking-id}", VueComponent("<sleep-tracking-profile></sleep-tracking-profile>"))
            get("/userWeight/{user-weight-id}", VueComponent("<user-weight-profile></user-weight-profile>"))
            get("/moodTracking/{mood-tracking-id}", VueComponent("<mood-tracking-profile></mood-tracking-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            get("/users/{user-id}/fitnessGoals", VueComponent("<user-fitness-goals-overview></user-fitness-goals-overview>"))
            get("/users/{user-id}/weightGoals", VueComponent("<user-weight-goals-overview></user-weight-goals-overview>"))
            get("/users/{user-id}/nutritionGoals", VueComponent("<user-nutrition-goals-overview></user-nutrition-goals-overview>"))
            get("/users/{user-id}/healthRecords", VueComponent("<user-health-record-overview></user-health-record-overview>"))
            get("/users/{user-id}/sleepTracking", VueComponent("<user-sleep-tracking-overview></user-sleep-tracking-overview>"))
            get("/users/{user-id}/moodTracking", VueComponent("<user-mood-tracking-overview></mood-health-tracking-overview>"))
            get("/fitnessGoals/{fitness-goal-id}/activities", VueComponent("<fitness-goals-activity-overview></fitness-goals-activity-overview>"))
            get("/fitnessGoals/{fitness-goal-id}/nutritionGoals", VueComponent("<fitness-goals-nutrition-goal-overview></fitness-goals-nutrition-goal-overview>"))
            get("/activities/{activity-id}/weightGoals", VueComponent("<activity-weight-goal-overview></activity-weight-goal-overview>"))
            get("/weightGoals/{weight-goal-id}/nutritionGoals", VueComponent("<weight-goal-nutrition-goal-overview></weight-goal-nutrition-goal-overview>"))
            get("/weightGoals/{weight-goal-id}/userWeight", VueComponent("<weight-goals-user-weight-overview></weight-goals-user-weight-overview>"))
            get("/sleepTracking/{sleep-tracking-id}/moodTracking", VueComponent("<sleep-tracking-mood-tracking-overview></sleep-tracking-mood-tracking-overview>"))
        }
    }
}