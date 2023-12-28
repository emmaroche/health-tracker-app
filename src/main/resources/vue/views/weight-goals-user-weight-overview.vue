<template id="weight-goals-user-weight-overview">
  <app-layout>
    <div class="card bg-light mt-4 mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">Current Weight</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-sm" @click="hideForm = !hideForm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-plus" aria-hidden="true"></i> Add
            </button>
          </div>
        </div>
      </div>
      <div class="col-12 ml-2 mb-3 mt-3" style="font-weight: 400;">  Add current weight.</div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addUserWeight">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-weeklyGoal">Current Weight (kg)</span>
            </div>
            <input type="text" class="form-control" v-model="formData.currentWeight" name="currentWeight" placeholder="Current Weight"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-deadline">Current Weight Timestamp</span>
            </div>
            <input type="date" class="form-control" v-model="formData.currentWeightTimestamp" name="currentWeightTimestamp" placeholder="Current Weight Timestamp"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-userId">Weight Goal ID</span>
            </div>
            <input type="text" class="form-control" v-model="formData.weightGoalId" name="weightGoalId" placeholder="Weight Goal ID"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-userId">User ID</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>

        </form>
        <button rel="tooltip" title="Add Weight Goal" class="btn btn-info btn-sm mt-3" @click="addWeight" style="background-color: #08a29e; border-color: #08a29e;">
          <i class="fa fa-plus" aria-hidden="true"></i> Add Weight
        </button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="col-6 mb-3" style="font-weight: 600;">Current Weight Goals</div>
      <div class="list-group-item d-flex align-items-start" v-for="(currentWeight, index) in userWeight" :key="index">
        <div class="mr-auto p-2">
          <span>
            <a :href="`/userWeight/${currentWeight.id}`" style="color: #08a29e;">
              Current Weight: {{ currentWeight.currentWeight }} (Weight ID: {{ currentWeight.weightGoalId }}, User ID: {{ currentWeight.userId }})
            </a>
          </span>
        </div>
        <div class="p-2">
          <div class="btn-group d-flex" role="group">
            <a :href="`/userWeight/${currentWeight.id}`" class="btn btn-info btn-sm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </a>
            <div class="mr-2"></div>
            <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm" @click="deleteWeightGoal(currentWeight, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
        </div>
      </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 200px;
}
</style>

<script>
app.component("weight-goals-user-weight-overview", {
  template: "#weight-goals-user-weight-overview",
  data: () => ({
    userWeight: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchWeightGoals();
  },
  methods: {
    fetchWeightGoals: function () {
      axios.get("/api/userWeight")
          .then(res => this.userWeight = res.data)
          .catch(() => alert("We couldn't find any user weights at the moment. Feel free to add a new weight, wait a moment, or refresh the page to check again"));
    },
    addWeightGoal: function () {
      const url = "/api/userWeight";
      axios.post(url, this.formData)
          .then(response => {
            this.userWeight.push(response.data);
            this.hideForm = true;
            // Reset form data after adding user weight
            this.formData = {};
          })
          .catch(error => {
            console.log(error);
          });
    },
    deleteWeightGoal: function (currentWeight, index) {
      if (confirm('Are you sure you want to delete this weight? This action cannot be undone.', 'Warning')) {
        const userWeightId = currentWeight.id;
        const url = `/api/userWeight/${userWeightId}`;
        axios.delete(url)
            .then(response =>
                this.userWeight.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error);
            });
      }
    },
  }
});
</script>
