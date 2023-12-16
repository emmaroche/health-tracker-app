<template id="fitness-goals-profile">
  <app-layout>
    <div v-if="noFitnessGoal">
      <p>We're sorry, we were not able to retrieve this fitness goal.</p>
      <p>View <a :href="'/fitnessGoals'">all fitness goals</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noFitnessGoal">
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
    fitnessGoal: null,
    noFitnessGoal: false
  }),
  created: function () {
    const fgId = this.$javalin.pathParams["fitness-goal-id"];
    const url = `/api/fitnessGoals/${fgId}`;
    axios.get(url)
        .then(res => this.fitnessGoal = res.data)
        .catch(() => {
          console.error("Error while fetching fitness goal: " + fgId);
          this.noFitnessGoal = true;
        });
  },
  methods: {
    updateFitnessGoal: function () {
      const fgId = this.$javalin.pathParams["fitness-goal-id"];
      const url = `/api/fitnessGoals/${fgId}`;

      const updatedFitnessGoal = {
        id: this.fitnessGoal.id,
        type: this.fitnessGoal.type,
        workoutsPerWeek: this.fitnessGoal.workoutsPerWeek,
        minutesOfWorkouts: this.fitnessGoal.minutesOfWorkouts,
        calorieBurningGoalDuringExercise: this.fitnessGoal.calorieBurningGoalDuringExercise,
        userId: this.fitnessGoal.userId
      };

      axios.patch(url, updatedFitnessGoal)
          .then(response => {
            // Extract the data from the response
            const responseData = response.data;

            // Update the fitnessGoal with the extracted data
            Object.assign(this.fitnessGoal, responseData);

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
              window.location.href = '/fitnessGoals';
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
