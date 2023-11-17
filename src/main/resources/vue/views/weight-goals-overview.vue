<template id="weight-goals-overview">
  <app-layout>
    <div>
      <div class="card bg-light mb-3">
        <div class="card-header">
          <div class="row">
            <div class="col-6">
              Weight Goals
            </div>
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
          <form id="addWeightGoal">
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-weight-goal-type">Type</span>
              </div>
              <input type="text" class="form-control" v-model="formData.type" name="type" placeholder="Type"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-weight-goal-startingWeight">Starting Weight</span>
              </div>
              <input type="text" class="form-control" v-model="formData.startingWeight" name="startingWeight" placeholder="Starting Weight"/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-weight-goal-currentWeight">Current Weight</span>
              </div>
              <input type="text" class="form-control" v-model="formData.currentWeight" name="currentWeight" placeholder="Current Weight"/>
            </div>

            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-weight-goal-targetWeight">Target Weight</span>
              </div>
              <input type="text" class="form-control" v-model="formData.targetWeight" name="targetWeight" placeholder="Target Weight"/>
            </div>

            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-weight-goal-weeklyGoal">Weekly Goal</span>
              </div>
              <input type="text" class="form-control" v-model="formData.weeklyGoal" name="weeklyGoal" placeholder="Weekly Goal"/>
            </div>

            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-weight-goal-deadline">Deadline</span>
              </div>
              <input type="text" class="form-control" v-model="formData.deadline" name="deadline" placeholder="Deadline"/>
            </div>

            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-weight-goal-userId">User ID</span>
              </div>
              <input type="text" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
            </div>
          </form>
          <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addWeightGoal()">Add Weight Goal</button>
        </div>
      </div>
      <div class="list-group list-group-flush">
        <div class="list-group-item d-flex align-items-start"
             v-for="(weightGoal,index) in weightGoals" :key="index">
          <div class="mr-auto p-2">
            <span><a :href="`/weightGoals/${weightGoal.id}`"> {{ weightGoal.type }} (Starting Weight: {{ weightGoal.startingWeight }}, Current Weight: {{ weightGoal.currentWeight }}, Target Weight: {{ weightGoal.targetWeight }}, Weekly Goal: {{ weightGoal.weeklyGoal }}, Deadline: {{ weightGoal.deadline }}, User ID: {{ weightGoal.userId }})</a></span>
          </div>
          <div class="p2">
            <a :href="`/weightGoals/${weightGoal.id}`">
              <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
                <i class="fa fa-pencil" aria-hidden="true"></i>
              </button>
            </a>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteWeightGoal(weightGoal, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("weight-goals-overview", {
  template: "#weight-goals-overview",
  data: () => ({
    weightGoals: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchWeightGoals();
  },
  methods: {
    fetchWeightGoals: function () {
      axios.get("/api/weightGoals")
          .then(res => this.weightGoals = res.data)
          .catch(() => alert("Error while fetching weight goals"));
    },
    addWeightGoal: function () {
      const url = "/api/weightGoals";
      axios.post(url, this.formData)
          .then(response => {
            this.weightGoals.push(response.data);
            this.hideForm = true;
            // Reset form data after adding weight goal
            this.formData = {};
          })
          .catch(error => {
            console.log(error);
          });
    },
    deleteWeightGoal: function (weightGoal, index) {
      if (confirm('Are you sure you want to delete this weight goal? This action cannot be undone.', 'Warning')) {
        // User confirmed delete
        const weightGoalId = weightGoal.id;
        const url = `/api/weightGoals/${weightGoalId}`;
        axios.delete(url)
            .then(response =>
                // Delete from the local state so Vue will reload the list automatically
                this.weightGoals.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error);
            });
      }
    },
  }
});
</script>
