<template id="fitness-goals-profile">
  <app-layout>
    <div v-if="noFitnessGoal">
      <p>We're sorry, we were not able to retrieve this fitness goal.</p>
      <p>View <a :href="'/fitnessGoals'">all fitness goals</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noFitnessGoal">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;"> Fitness Goal Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateFitnessGoal()"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteFitnessGoal()"
            >
              <i class="fas fa-trash" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="card-body">
        <form>
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-description">Type</span>
          </div>
          <input class="form-control" v-model="fitnessGoal.type" name="type" type="text" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Workouts Per Week</span>
          </div>
          <input class="form-control" v-model="fitnessGoal.workoutsPerWeek" name="workoutsPerWeek" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-calories">Workout Duration (minutes)</span>
          </div>
          <input class="form-control" v-model="fitnessGoal.minutesOfWorkouts" name="minutesOfWorkouts" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Calorie Burning Goal</span>
          </div>
          <input class="form-control" v-model="fitnessGoal.calorieBurningGoalDuringExercise" name="calorieBurningGoalDuringExercise" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-userId">User ID</span>
          </div>
          <input class="form-control" v-model="fitnessGoal.userId" name="userId" type="number" />
        </div>
      </form>
      </div>
      <div class="card-footer text-left">
        <p v-if="activities.length === 0">
          No activities or goals associated yet
        </p>
        <p v-else>
          View associated activities and nutrition goals you have set to assist in achieving your fitness goal:
        </p>
        <div class="card-footer text-center">
          <div v-if="fitnessGoal">
            <div class="btn-group-vertical" role="group" aria-label="Fitness Actions">
              <a :href="`/fitnessGoals/${fitnessGoal.id}/activities`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-running"></i> View User Activities
              </a>
              <a :href="`/fitnessGoals/${fitnessGoal.id}/nutritionGoals`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-apple-alt"></i> View Nutrition Goals
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 230px;
}
</style>

<script>
app.component("fitness-goals-profile", {
  template: "#fitness-goals-profile",
  data: () => ({
    fitnessGoal: null,
    noFitnessGoal: false,
    activities: [],
    nutritionGoals: [],
  }),
  created: function () {
    const fgId = this.$javalin.pathParams["fitness-goal-id"];
    const url = `/api/fitnessGoals/${fgId}`;

    console.log("Fitness Goal ID:", fgId);

// Fetch the fitness goal
    axios.get(url)
        .then(res => {
          console.log("Fitness Goal Response:", res.data);
          this.fitnessGoal = res.data;
        })
        .catch(error => {
          console.error("Error while fetching fitness goal:", error);
          this.noFitnessGoal = true;
        });

    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet: " + error)
        })

    axios.get(url + `/nutritionGoals`)
        .then(res => this.nutritionGoals = res.data)
        .catch(error => {
          console.log("No nutrition goals added yet: " + error)
        })
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
