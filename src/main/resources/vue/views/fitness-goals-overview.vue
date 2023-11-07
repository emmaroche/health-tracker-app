<template id="fitness-goals-overview">
  <app-layout>
    <div>
      <div>
        <ul class="fitness-goals-overview-list">
          <li v-for="fitnessGoal in fitnessGoals">
            <a :href="`/fitnessGoals/${fitnessGoal.id}`">{{ fitnessGoal.type }} (Workouts per week: {{ fitnessGoal.workoutsPerWeek }}, Minutes of Workouts: {{ fitnessGoal.minutesOfWorkouts }}, Calorie Burning Goal: {{ fitnessGoal.calorieBurningGoalDuringExercise }}, User ID: {{ fitnessGoal.userId }})</a>
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("fitness-goals-overview", {
  template: "#fitness-goals-overview",
  data: () => ({
    fitnessGoals: [],
  }),
  created() {
    this.fetchFitnessGoals();
  },
  methods: {
    fetchFitnessGoals: function () {
      axios.get("/api/fitnessGoals")
          .then(res => this.fitnessGoals = res.data)
          .catch(() => alert("Error while fetching fitness goals"));
    }
  }
});
</script>