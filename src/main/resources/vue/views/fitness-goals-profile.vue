<template id="fitness-goals-profile">
  <app-layout>
    <div>
      <form v-if="fitnessGoal">
        <label class="col-form-label">Goal ID:</label>
        <input class="form-control" v-model="fitnessGoal.id" name="id" type="number" readonly/><br>
        <label class="col-form-label">Type:</label>
        <input class="form-control" v-model="fitnessGoal.type" name="type" type="text"/><br>
        <label class="col-form-label">Workouts Per Week:</label>
        <input class="form-control" v-model="fitnessGoal.workoutsPerWeek" name="workoutsPerWeek" type="number"/><br>
        <label class="col-form-label">Minutes of Workouts:</label>
        <input class="form-control" v-model="fitnessGoal.minutesOfWorkouts" name="minutesOfWorkouts" type="number"/><br>
        <label class="col-form-label">Calorie Burning Goal:</label>
        <input class="form-control" v-model="fitnessGoal.calorieBurningGoalDuringExercise" name="calorieBurningGoalDuringExercise" type="number"/><br>
        <label class="col-form-label">User ID:</label>
        <input class="form-control" v-model="fitnessGoal.userId" name="userId" type="number"/><br>
      </form>
    </div>
  </app-layout>
</template>

<script>
app.component("fitness-goals-profile", {
  template: "#fitness-goals-profile",
  data: () => ({
    fitnessGoal: null
  }),
  created: function () {
    const fgId = this.$javalin.pathParams["fitness-goal-id"];
    const url = `/api/fitnessGoals/${fgId}`;
    console.log("Fetching fitness goal data from URL: " + url);
    axios.get(url)
        .then(res => {
          console.log("Received data:", res.data);
          this.fitnessGoal = res.data;
        })
        .catch(error => {
          console.error("Error while fetching fitness goal: " + fgId, error);
          alert("Error while fetching fitness goal" + fgId);
        });
  }

});
</script>
