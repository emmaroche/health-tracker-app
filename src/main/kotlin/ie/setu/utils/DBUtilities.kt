package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToHealthRecord(it: ResultRow): HealthRecord {
    return HealthRecord(
        id = it[HealthRecords.id],
        timestamp = it[HealthRecords.timestamp],
        firstName = it[HealthRecords.firstName],
        lastName = it[HealthRecords.lastName],
        sex = it[HealthRecords.sex],
        dob = it[HealthRecords.dob],
        weight = it[HealthRecords.weight],
        height = it[HealthRecords.height],
        bloodType = it[HealthRecords.bloodType],
        allergies = it[HealthRecords.allergies],
        medicalConditions = it[HealthRecords.medicalConditions],
        medications = it[HealthRecords.medications],
        notes = it[HealthRecords.notes],
        userId = it[HealthRecords.userId]
    )
}

fun mapToFitnessGoal(it: ResultRow): FitnessGoal {
    return FitnessGoal(
        id = it[FitnessGoals.id],
        type = it[FitnessGoals.type],
        workoutsPerWeek = it[FitnessGoals.workoutsPerWeek],
        minutesOfWorkouts = it[FitnessGoals.minutesOfWorkouts],
        calorieBurningGoalDuringExercise = it[FitnessGoals.calorieBurningGoalDuringExercise],
        userId = it[FitnessGoals.userId]
    )
}

fun mapToWeightGoal(it: ResultRow): WeightGoal {
    return WeightGoal(
        id = it[WeightGoals.id],
        type = it[WeightGoals.type],
        startingWeight = it[WeightGoals.startingWeight],
        startingWeightTimestamp = it[WeightGoals.startingWeightTimestamp],
        currentWeight = it[WeightGoals.currentWeight],
        targetWeight = it[WeightGoals.targetWeight],
        weeklyGoal = it[WeightGoals.weeklyGoal],
        deadline = it[WeightGoals.deadline],
        userId = it[WeightGoals.userId]
    )
}

fun mapToNutritionGoal(it: ResultRow): NutritionGoal {
    return NutritionGoal(
        id = it[NutritionGoals.id],
        type = it[NutritionGoals.type],
        proteinGoal = it[NutritionGoals.proteinGoal],
        fibreGoal = it[NutritionGoals.fibreGoal],
        calorieGoal = it[NutritionGoals.calorieGoal],
        carbsGoal = it[NutritionGoals.carbsGoal],
        fatGoal = it[NutritionGoals.fatGoal],
        userId = it[NutritionGoals.userId]
    )
}

fun mapToSleepTracking(it: ResultRow): SleepEntry {
    return SleepEntry(
        id = it[SleepTracking.id],
        date = it[SleepTracking.date],
        duration = it[SleepTracking.duration],
        quality = it[SleepTracking.quality],
        notes = it[SleepTracking.notes],
        userId = it[SleepTracking.userId]
    )
}
