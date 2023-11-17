<template id="fitness-goals-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">Fitness Goals</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addFitnessGoal">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-goal-type">Type</span>
            </div>
            <input type="text" class="form-control" v-model="formData.type" name="type" placeholder="Type"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-goal-workouts">Workouts per week</span>
            </div>
            <input type="number" class="form-control" v-model="formData.workoutsPerWeek" name="workoutsPerWeek" placeholder="Workouts per week"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-goal-minutes">Minutes of Workouts</span>
            </div>
            <input type="number" class="form-control" v-model="formData.minutesOfWorkouts" name="minutesOfWorkouts" placeholder="Minutes of Workouts"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-goal-calories">Calorie Burning Goal</span>
            </div>
            <input type="number" class="form-control" v-model="formData.calorieBurningGoalDuringExercise" name="calorieBurningGoalDuringExercise" placeholder="Calorie Burning Goal"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-fitness-goal-userId">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addFitnessGoal()">Add Fitness Goal</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(fitnessGoal, index) in fitnessGoals" :key="index">
        <div class="mr-auto p-2">
      <span>
        <a :href="`/fitnessGoals/${fitnessGoal.id}`">
          {{ fitnessGoal.type }} (Workouts per week: {{ fitnessGoal.workoutsPerWeek }}, Minutes of Workouts: {{ fitnessGoal.minutesOfWorkouts }}, Calorie Burning Goal: {{ fitnessGoal.calorieBurningGoalDuringExercise }}, User ID: {{ fitnessGoal.userId }})
        </a>
      </span>
        </div>
        <div class="p-2">
          <a :href="`/fitnessGoals/${fitnessGoal.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteFitnessGoal(fitnessGoal, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("fitness-goals-overview", {
  template: "#fitness-goals-overview",
  data: () => ({
    fitnessGoals: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchFitnessGoals();
  },
  methods: {
    fetchFitnessGoals: function () {
      axios.get("/api/fitnessGoals")
          .then(res => this.fitnessGoals = res.data)
          .catch(() => alert("Error while fetching fitness goals"));
    },
    deleteFitnessGoal: function (fitnessGoal, index) {
      if (confirm('Are you sure you want to delete this fitness goal? This action cannot be undone.', 'Warning')) {
        const fitnessGoalId = fitnessGoal.id;
        const url = `/api/fitnessGoals/${fitnessGoalId}`;
        axios.delete(url)
            .then(response =>
                this.fitnessGoals.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addFitnessGoal: function () {
      const url = "/api/fitnessGoals";
      axios.post(url, this.formData)
          .then(response => {
            this.fitnessGoals.push(response.data);
            this.hideForm = true;
            // Reset form data after adding fitness goal
            this.formData = {
              type: '',
              workoutsPerWeek: null,
              minutesOfWorkouts: null,
              calorieBurningGoalDuringExercise: null,
              userId: null,
              duration: null,
              targetWeight: null,
              targetFatPercentage: null,
              // Add more properties as needed
            };
          })
          .catch(error => {
            console.log(error);
          });
    },
  }
});
</script>
