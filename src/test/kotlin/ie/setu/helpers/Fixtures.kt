package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.FitnessGoal
import ie.setu.domain.HealthRecord
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.FitnessGoals
import ie.setu.domain.db.Users
import ie.setu.domain.db.HealthRecords
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FitnessGoalsDAO
import ie.setu.domain.repository.HealthRecordDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 945
val updatedStarted = DateTime.parse("2023-10-25T14:36:23.915Z")

val updatedTimestamp = DateTime.parse("2023-10-25T15:29:45.225Z")
val updatedFirstname = "Joe"
val updatedLastname = "Smith"
val updatedSex= "Male"
val updatedDob = DateTime.parse("1987-06-11T05:59:27.258Z")
val updatedWeight = 90.0
val updatedHeight = 170
val updatedbloodType = "O"
val updatedallergies = "Cats"
val updatedmedicalConditions = "None"
val updatedmedications = "None"
val updatednotes = "None"

val updatedType = "Cardio"
val updatedWorkoutsPerWeek = 4
val updatedMinutesOfWorkouts = 35
val updatedCalorieBurningGoalDuringExercise = 550.0
val updatedUserId = 1

val users = arrayListOf(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf(
    Activity(description = "Running", duration = 60.0, calories = 300, started = DateTime.now(), userId = 1, id = 1),
    Activity(description = "Swimming", duration = 45.5, calories = 250, started = DateTime.now(), userId = 2, id = 2),
    Activity(description = "Cycling", duration = 90.0, calories = 400, started = DateTime.now(), userId = 2, id = 3),
    Activity(description = "Hiking", duration = 120.0, calories = 600, started = DateTime.now(), userId = 4, id = 4)
)

val healthRecords = arrayListOf(
    HealthRecord(
        timestamp = DateTime.now(),
        firstName = "John",
        lastName = "Doe",
        sex = "Male",
        dob = DateTime(635734800000),
        weight = 75.5,
        height = 175,
        bloodType = "A+",
        allergies = "None",
        medicalConditions = "None",
        medications = "None",
        notes = "Regular checkup",
        userId = 1,
        id = 1
    ),
    HealthRecord(
        timestamp = DateTime.now(),
        firstName = "Jane",
        lastName = "Smith",
        sex = "Female",
        dob = DateTime(634877400000),
        weight = 65.0,
        height = 160,
        bloodType = "B-",
        allergies = "Pollen",
        medicalConditions = "None",
        medications = "Antihistamine",
        notes = "Allergy follow-up",
        userId = 2,
        id = 2
    ),
    HealthRecord(
        timestamp = DateTime.now(),
        firstName = "Michael",
        lastName = "Johnson",
        sex = "Male",
        dob = DateTime(627420000000),
        weight = 82.3,
        height = 185,
        bloodType = "O+",
        allergies = "None",
        medicalConditions = "Hypertension",
        medications = "Lisinopril",
        notes = "Hypertension management",
        userId = 3,
        id = 3
    ),
    HealthRecord(
        timestamp = DateTime.now(),
        firstName = "Emily",
        lastName = "Davis",
        sex = "Female",
        dob = DateTime(640000000000),
        weight = 68.7,
        height = 170,
        bloodType = "AB+",
        allergies = "Dust",
        medicalConditions = "None",
        medications = "None",
        notes = "Routine health check",
        userId = 1,
        id = 4
    )
)

val fitnessGoals = arrayListOf(
    FitnessGoal(
        id = 1,
        type = "Cardio",
        workoutsPerWeek = 3,
        minutesOfWorkouts = 30,
        calorieBurningGoalDuringExercise = 500.0,
        userId = 1
    ),
    FitnessGoal(
        id = 2,
        type = "Strength Training",
        workoutsPerWeek = 4,
        minutesOfWorkouts = 45,
        calorieBurningGoalDuringExercise = 600.0,
        userId = 2
    ),
    FitnessGoal(
        id = 3,
        type = "Yoga",
        workoutsPerWeek = 2,
        minutesOfWorkouts = 60,
        calorieBurningGoalDuringExercise = 300.0,
        userId = 3
    ),
    FitnessGoal(
        id = 4,
        type = "Running",
        workoutsPerWeek = 5,
        minutesOfWorkouts = 40,
        calorieBurningGoalDuringExercise = 700.0,
        userId = 1
    )
)


fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}

fun populateHRTable(): HealthRecordDAO {
    SchemaUtils.create(HealthRecords)
    val hrDAO = HealthRecordDAO()
    hrDAO.save(healthRecords[0])
    hrDAO.save(healthRecords[1])
    hrDAO.save(healthRecords[2])
    return hrDAO
}

fun populateFGTable(): FitnessGoalsDAO {
    SchemaUtils.create(FitnessGoals)
    val fgDAO = FitnessGoalsDAO()
    fgDAO.save(fitnessGoals[0])
    fgDAO.save(fitnessGoals[1])
    fgDAO.save(fitnessGoals[2])
    return fgDAO
}