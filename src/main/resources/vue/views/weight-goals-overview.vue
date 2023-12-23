<template id="weight-goals-overview">
  <app-layout>
    <div class="card bg-light mt-4 mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">Weight Goals</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-sm" @click="hideForm = !hideForm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-plus" aria-hidden="true"></i> Add
            </button>
          </div>
        </div>
      </div>
      <div class="col-12 ml-2 mb-3 mt-3" style="font-weight: 400;">  Work towards your target weight with our Weight Goals feature.
        Easily manage your goals, and witness positive changes in your health!</div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addWeightGoal">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-type">Type</span>
            </div>
            <input type="text" class="form-control" v-model="formData.type" name="type" placeholder="Type"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-startingWeight">Starting Weight (kg)</span>
            </div>
            <input type="text" class="form-control" v-model="formData.startingWeight" name="startingWeight" placeholder="Starting Weight"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-targetWeight">Target Weight (kg)</span>
            </div>
            <input type="text" class="form-control" v-model="formData.targetWeight" name="targetWeight" placeholder="Target Weight"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-weeklyGoal">Weekly Goal (kg)</span>
            </div>
            <input type="text" class="form-control" v-model="formData.weeklyGoal" name="weeklyGoal" placeholder="Weekly Goal"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-deadline">Deadline</span>
            </div>
            <input type="text" class="form-control" v-model="formData.deadline" name="deadline" placeholder="Deadline"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-userId">User ID</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-actId">Activity ID</span>
            </div>
            <input type="text" class="form-control" v-model="formData.actId" name="userId" placeholder="Activity ID"/>
          </div>

        </form>
        <button rel="tooltip" title="Add Weight Goal" class="btn btn-info btn-sm mt-3" @click="addWeightGoal" style="background-color: #08a29e; border-color: #08a29e;">
          <i class="fa fa-plus" aria-hidden="true"></i> Add Weight Goal
        </button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="col-6 mb-3" style="font-weight: 600;">Current Weight Goals</div>
      <div class="list-group-item d-flex align-items-start" v-for="(weightGoal, index) in weightGoals" :key="index">
        <div class="mr-auto p-2">
          <span>
            <a :href="`/weightGoals/${weightGoal.id}`" style="color: #08a29e;">
              {{ weightGoal.type }} (Starting Weight: {{ weightGoal.startingWeight }} kg, Target Weight: {{ weightGoal.targetWeight }} kg, Weekly Goal: {{ weightGoal.weeklyGoal }} kg, Deadline: {{ weightGoal.deadline }}, User ID: {{ weightGoal.userId }})
            </a>
          </span>
        </div>
        <div class="p-2">
          <div class="btn-group d-flex" role="group">
            <a :href="`/weightGoals/${weightGoal.id}`" class="btn btn-info btn-sm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </a>
            <div class="mr-2"></div>
            <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm" @click="deleteWeightGoal(weightGoal, index)">
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
.description-text {
  font-size: 16px;
  color: #495057;
}
</style>

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
        const weightGoalId = weightGoal.id;
        const url = `/api/weightGoals/${weightGoalId}`;
        axios.delete(url)
            .then(response =>
                this.weightGoals.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error);
            });
      }
    },
  }
});
</script>
