<template id="user-fitness-goals-overview">
  <app-layout>
    <div>
      <h3>Fitness Goals List</h3>
      <ul>
        <li v-for="fitnessGoal in fitnessGoals">
          {{ fitnessGoal.id }}: {{ fitnessGoal.type }} (Workouts per week: {{ fitnessGoal.workoutsPerWeek }}, Minutes of Workouts: {{ fitnessGoal.minutesOfWorkouts }}, Calorie Burning Goal: {{ fitnessGoal.calorieBurningGoalDuringExercise }}, User ID: {{ fitnessGoal.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-fitness-goals-overview",{
  template: "#user-fitness-goals-overview",
  data: () => ({
    fitnessGoals: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/fitnessGoals`)
        .then(res => this.fitnessGoals = res.data)
        .catch(() => alert("We couldn't find any fitness goals at the moment. Feel free to add a new fitness goal, wait a moment, or refresh the page to check again"));
  }
});
</script>