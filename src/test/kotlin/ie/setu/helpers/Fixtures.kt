package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
const val validName = "Test User 1"
const val validEmail = "testuser1@test.com"
const val validPhone = 123456789
const val validAddress = "123 Main Street"
const val updatedName = "Updated Name"
const val updatedEmail = "Updated Email"
const val updatedPhone = 123456789
const val updatedAddress = "123 Main Street"

const val updatedDescription = "Updated Description"
const val updatedDuration = 30.0
const val updatedCalories = 945
val updatedStarted: DateTime = DateTime.parse("2023-10-25T14:36:23.915Z")

val updatedTimestamp: DateTime = DateTime.parse("2023-10-25T15:29:45.225Z")
const val updatedFirstname = "Joe"
const val updatedLastname = "Smith"
const val updatedSex = "Male"
val updatedDob: DateTime = DateTime.parse("1987-06-11T05:59:27.258Z")
const val updatedWeight = 90.0
const val updatedHeight = 170
const val updatedBloodType = "O"
const val updatedAllergies = "Cats"
const val updatedMedicalConditions = "None"
const val updatedMedications = "None"
const val updatedNote = "None"

const val updatedType = "Cardio"
const val updatedWorkoutsPerWeek = 4
const val updatedMinutesOfWorkouts = 35
const val updatedCalorieBurningGoalDuringExercise = 550.0

const val updatedType2 = "Weight Loss"
const val updatedStartingWeight = 75.0
val updatedStartingWeightTimestamp: DateTime = DateTime.parse("2023-11-23T10:30:00")
const val updatedTargetWeight = 70.0
const val updatedWeeklyGoal = 1.5
val updatedDeadline: DateTime = DateTime.parse("2023-11-23T10:30:00")

const val updatedType3 = "Weight Loss"
const val updatedProteinGoal = 120.0
const val updatedFibreGoal = 30.0
const val updatedCalorieGoal = 1500.0
const val updatedCarbsGoal = 200.0
const val updatedFatGoal = 50.0

val updatedDate: DateTime = DateTime.parse("2023-10-25T14:36:23.915Z")
const val updatedDuration2 = 30
const val updatedQuality = "Updated Quality"
const val updatedNotes = "Updated Notes"

const val updatedMood = "Sad"
const val updatedRating = 1

val users = arrayListOf(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", phoneNumber = 897765679, address = "123 Wonderland St, Wonderland City", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", phoneNumber = 123456789, address = "456 Cat Ave, Cat Town", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", phoneNumber = 987654321, address = "789 Contrary Blvd, Contrary Village", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", phoneNumber = 555555555, address = "321 Singer Rd, Singer Hamlet", id = 4)
)

val activities = arrayListOf(
    Activity(description = "Running", duration = 60.0, calories = 300, started = DateTime.now(), userId = 1, fitnessId= 1, id = 1),
    Activity(description = "Swimming", duration = 45.5, calories = 250, started = DateTime.now(), userId = 2, fitnessId= 2, id = 2),
    Activity(description = "Cycling", duration = 90.0, calories = 400, started = DateTime.now(), userId = 2, fitnessId= 3, id = 3),
    Activity(description = "Hiking", duration = 120.0, calories = 600, started = DateTime.now(), userId = 4, fitnessId= 4, id = 4)
)

// The fixtures below were generated by ChatGPT to make the population process faster for testing

val healthRecords = arrayListOf(
    HealthRecord(
        timestamp = DateTime.now(),
        firstName = "John",
        lastName = "Doe",
        sex = "Male",
        dob = DateTime(635734800000),
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

val weightGoals = arrayListOf(
    WeightGoal(
        id = 1,
        type = "Weight Gain",
        startingWeight = 150.0,
        startingWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        targetWeight = 160.0,
        weeklyGoal = 0.5,
        deadline = DateTime.parse("2023-11-23T10:30:00"),
        userId = 1,
        actId = 1
    ),
    WeightGoal(
        id = 2,
        type = "Weight Loss",
        startingWeight = 180.0,
        startingWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        targetWeight = 170.0,
        weeklyGoal = 0.3,
        deadline = DateTime.parse("2023-11-23T10:30:00"),
        userId = 2,
        actId = 2
    ),
    WeightGoal(
        id = 3,
        type = "Maintain Weight",
        startingWeight = 160.0,
        startingWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        targetWeight = 160.0,
        weeklyGoal = 0.0,
        deadline = DateTime.parse("2023-11-23T10:30:00"),
        userId = 3,
        actId = 3
    ),
    WeightGoal(
        id = 284,
        type = "Maintain Weight",
        startingWeight = 120.0,
        startingWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        targetWeight = 160.0,
        weeklyGoal = 0.0,
        deadline = DateTime.parse("2023-11-23T10:30:00"),
        userId = 4,
        actId = 4
    )
)

val userWeight = arrayListOf(
    CurrentWeight(
        id = 1,
        currentWeight = 80.0,
        currentWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        weightGoalId = 2,
        userId = 2
    ),
    CurrentWeight(
        id = 2,
        currentWeight = 75.0,
        currentWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        weightGoalId = 2,
        userId = 2
    ),
    CurrentWeight(
        id = 3,
        currentWeight = 160.0,
        currentWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        weightGoalId = 2,
        userId = 3
    )
)

val userWeight2 = arrayListOf(
    CurrentWeight(
        id = 1,
        currentWeight = 80.0,
        currentWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        weightGoalId = 789,
        userId = 2
    ),
    CurrentWeight(
        id = 2,
        currentWeight = 75.0,
        currentWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        weightGoalId = 789,
        userId = 2
    ),
    CurrentWeight(
        id = 3,
        currentWeight = 160.0,
        currentWeightTimestamp = DateTime.parse("2023-11-23T10:30:00"),
        weightGoalId = 789,
        userId = 3
    )
)


val nutritionGoals = arrayListOf(
    NutritionGoal(
        id = 1,
        type = "Type1",
        proteinGoal = 30.0,
        fibreGoal = 10.0,
        calorieGoal = 500.0,
        carbsGoal = 40.0,
        fatGoal = 20.0,
        userId = 1,
        fitnessId = 1,
        weightId = 1
    ),
    NutritionGoal(
        id = 2,
        type = "Type2",
        proteinGoal = 40.0,
        fibreGoal = 15.0,
        calorieGoal = 600.0,
        carbsGoal = 45.0,
        fatGoal = 25.0,
        userId = 2,
        fitnessId = 2,
        weightId = 2
    ),
    NutritionGoal(
        id = 3,
        type = "Type3",
        proteinGoal = 20.0,
        fibreGoal = 5.0,
        calorieGoal = 300.0,
        carbsGoal = 30.0,
        fatGoal = 15.0,
        userId = 3,
        fitnessId = 3,
        weightId = 3
    ),
    NutritionGoal(
        id = 4,
        type = "Type4",
        proteinGoal = 50.0,
        fibreGoal = 20.0,
        calorieGoal = 700.0,
        carbsGoal = 50.0,
        fatGoal = 30.0,
        userId = 1,
        fitnessId = 3,
        weightId = 2
    )
)

val sleepTracking = arrayListOf(
    SleepEntry(
        id = 1,
        date = DateTime.parse("2023-12-12T22:30:00"),
        duration = 480,
        quality = "Deep",
        notes = "A restful night of sleep",
        userId = 1
    ),
    SleepEntry(
        id = 2,
        date = DateTime.parse("2023-12-13T00:45:00"),
        duration = 420,
        quality = "Moderate",
        notes = "Woke up once during the night",
        userId = 2
    ),
    SleepEntry(
        id = 3,
        date = DateTime.parse("2023-12-14T23:15:00"),
        duration = 510,
        quality = "Light",
        notes = "Interrupted sleep due to noise",
        userId = 3
    ),
    SleepEntry(
        id = 4,
        date = DateTime.parse("2023-12-15T21:00:00"),
        duration = 540,
        quality = "Deep",
        notes = "Long and undisturbed sleep",
        userId = 1
    )
)

val moodTracking = arrayListOf(
    MoodEntry(
        id = 1,
        date = DateTime.parse("2023-12-12T12:30:00"),
        mood = "Happy",
        rating = 5,
        notes = "Feeling great today!",
        userId = 1,
        sleepId = 1
    ),
    MoodEntry(
        id = 2,
        date = DateTime.parse("2023-12-13T09:45:00"),
        mood = "Neutral",
        rating = 3,
        notes = "Not much happening.",
        userId = 2,
        sleepId = 1
    ),
    MoodEntry(
        id = 3,
        date = DateTime.parse("2023-12-14T20:15:00"),
        mood = "Sad",
        rating = 2,
        notes = "Feeling a bit down.",
        userId = 3,
        sleepId = 1
    ),
    MoodEntry(
        id = 4,
        date = DateTime.parse("2023-12-15T18:00:00"),
        mood = "Excited",
        rating = 4,
        notes = "Looking forward to an event.",
        userId = 1,
        sleepId = 1
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
    // Populating FitnessGoals table
    populateFGTable()
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

fun populateWGTable(): WeightGoalsDAO {
    SchemaUtils.create(WeightGoals)
    val wgDAO = WeightGoalsDAO()
    wgDAO.save(weightGoals[0])
    wgDAO.save(weightGoals[1])
    wgDAO.save(weightGoals[2])
    return wgDAO
}

fun populateNGTable(): NutritionGoalsDAO {
    // Populating FitnessGoals and WeightGoals tables
    populateFGTable()
    populateWGTable()
    SchemaUtils.create(NutritionGoals)
    val ngDAO = NutritionGoalsDAO()
    ngDAO.save(nutritionGoals[0])
    ngDAO.save(nutritionGoals[1])
    ngDAO.save(nutritionGoals[2])
    return ngDAO
}

fun populateSTTable(): SleepTrackingDAO {
    SchemaUtils.create(SleepTracking)
    val stDAO = SleepTrackingDAO()
    stDAO.save(sleepTracking[0])
    stDAO.save(sleepTracking[1])
    stDAO.save(sleepTracking[2])
    return stDAO
}

fun populateMTTable(): MoodTrackingDAO {
    SchemaUtils.create(SleepTracking)
    // Populating SleepTracking table
    val stDAO = populateSTTable()
    SchemaUtils.create(MoodTracking)
    val mtDAO = MoodTrackingDAO()
    mtDAO.save(moodTracking[0].copy(sleepId = stDAO.getAll().first().id))
    mtDAO.save(moodTracking[1].copy(sleepId = stDAO.getAll().first().id))
    mtDAO.save(moodTracking[2].copy(sleepId = stDAO.getAll().first().id))
    return mtDAO
}


fun populateUWTable(): UserWeightDAO {
    SchemaUtils.create(UserWeight, WeightGoals)

    // Populate WeightGoals table first as this was causing errors
    populateWGTable()

    val uwDAO = UserWeightDAO()
    uwDAO.save(userWeight[0])
    uwDAO.save(userWeight[1])
    uwDAO.save(userWeight[2])

    return uwDAO
}


