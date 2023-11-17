<template id="fitness-goals-profile">
  <app-layout>
    <div v-if="!fitnessGoal">
      <p>We're sorry, we were not able to retrieve this fitness goal.</p>
      <p>View <a :href="'/fitness-goals'">all fitness goals</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="fitnessGoal">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Fitness Goal Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link"
                @click="updateFitnessGoal()"
            >
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteFitnessGoal()"
            >
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="form-group">
            <label class="col-form-label">Goal ID:</label>
            <input class="form-control" v-model="fitnessGoal.id" name="id" type="number" readonly />
          </div>
          <div class="form-group">
            <label class="col-form-label">Type:</label>
            <input class="form-control" v-model="fitnessGoal.type" name="type" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Workouts Per Week:</label>
            <input class="form-control" v-model="fitnessGoal.workoutsPerWeek" name="workoutsPerWeek" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Minutes of Workouts:</label>
            <input class="form-control" v-model="fitnessGoal.minutesOfWorkouts" name="minutesOfWorkouts" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Calorie Burning Goal:</label>
            <input class="form-control" v-model="fitnessGoal.calorieBurningGoalDuringExercise" name="calorieBurningGoalDuringExercise" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">User ID:</label>
            <input class="form-control" v-model="fitnessGoal.userId" name="userId" type="number" />
          </div>
        </form>
      </div>
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
    axios.get(url)
        .then(res => this.fitnessGoal = res.data)
        .catch(() => {
          console.error("Error while fetching fitness goal: " + fgId);
          this.fitnessGoal = null;
        });
  },
  methods: {
    updateFitnessGoal: function () {
      const fgId = this.$javalin.pathParams["fitness-goal-id"];
      const url = `/api/fitnessGoals/${fgId}`;
      axios.patch(url, this.fitnessGoal)
          .then(response => {
            this.fitnessGoal = response.data;
            alert("Fitness Goal updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating Fitness Goal");
          });
    },
    deleteFitnessGoal: function () {
      if (confirm("Do you really want to delete this fitness goal?")) {
        const fgId = this.$javalin.pathParams["fitness-goal-id"];
        const url = `/api/fitnessGoals/${fgId}`;
        axios.delete(url)
            .then(() => {
              alert("Fitness Goal deleted");
              // Redirect to the fitness goals endpoint
              window.location.href = '/fitness-goals';
            })
            .catch(error => {
              console.log(error);
              alert("Error deleting Fitness Goal");
            });
      }
    }
  }
});
</script>
