<template id="weight-goals-profile">
  <app-layout>
    <div v-if="noWeightGoal">
      <p>We're sorry, we were not able to retrieve this weight goal.</p>
      <p>View <a :href="'/weightGoals'">all weight goals</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noWeightGoal">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">Weight Goal Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateWeightGoal"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteWeightGoal"
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
          <input class="form-control" v-model="weightGoal.type" name="type" type="text" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Starting Weight (kg)</span>
          </div>
          <input class="form-control" v-model="weightGoal.startingWeight" name="startingWeight" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-calories">Starting Weight Timestamp</span>
          </div>
          <input class="form-control" v-model="weightGoal.startingWeightTimestamp" name="startingWeightTimestamp" type="datetime-local" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Target Weight (kg)</span>
          </div>
          <input class="form-control" v-model="weightGoal.targetWeight" name="targetWeight" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Weekly Goal (kg)</span>
          </div>
          <input class="form-control" v-model="weightGoal.weeklyGoal" name="weeklyGoal" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-deadline">Deadline</span>
          </div>
          <input class="form-control" v-model="weightGoal.deadline" name="deadline" type="datetime-local" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-userId">User ID</span>
          </div>
          <input class="form-control" v-model="weightGoal.userId" name="userId" type="number" />
        </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-actId">Activity ID</span>
            </div>
            <input type="text" class="form-control" v-model="weightGoal.actId" name="userId" placeholder="Activity ID"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-center">
        <div v-if="weightGoal">
          <br />
          <a :href="`/weightGoals/${weightGoal.id}/userWeight`">View Current Weight</a>
          <br />
          <br />
        </div>
      </div>
      <div class="card-footer text-left">
        <p v-if="nutritionGoals.length === 0">
          No nutrition goals associated with this weight goal yet
        </p>
        <p v-else>
          View associated nutrition goal you have set to assist in achieving your weight goal:
        </p>
        <div class="card-footer text-center">
          <div v-if="weightGoal">
            <div class="btn-group-vertical" role="group" aria-label="Fitness Actions">
              <a :href="`/weightGoals/${weightGoal.id}/nutritionGoals`" class="btn btn-link" style="color: #08a29e;">
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
app.component("weight-goals-profile", {
  template: "#weight-goals-profile",
  data: () => ({
    weightGoal: null,
    noWeightGoal: false,
    nutritionGoals: []
  }),
  created: function () {
    const wgId = this.$javalin.pathParams["weight-goal-id"];
    const url = `/api/weightGoals/${wgId}`;
    axios.get(url)
        .then(res => this.weightGoal = res.data)
        .catch(() => {
          console.error("Error while fetching weight goal: " + wgId);
          this.noWeightGoal = true;
        });

    axios.get(url + `/nutritionGoals`)
        .then(res => this.nutritionGoals = res.data)
        .catch(error => {
          console.log("No nutrition goals added yet: " + error)
        })
  },
  methods: {
    updateWeightGoal: function () {
      const wgId = this.$javalin.pathParams["weight-goal-id"];
      const url = `/api/weightGoals/${wgId}`;

      const updatedWeightGoal = {
        id: this.weightGoal.id,
        type: this.weightGoal.type,
        startingWeight: this.weightGoal.startingWeight,
        startingWeightTimestamp: this.weightGoal.startingWeightTimestamp,
        targetWeight: this.weightGoal.targetWeight,
        weeklyGoal: this.weightGoal.weeklyGoal,
        deadline: this.weightGoal.deadline,
        userId: this.weightGoal.userId,
        actId: this.weightGoal.actId
      };

      axios.patch(url, updatedWeightGoal)
          .then(response => {
            const responseData = response.data;
            Object.assign(this.weightGoal, responseData);
            alert("Weight Goal updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating Weight Goal");
          });
    },
    deleteWeightGoal: function () {
      if (confirm("Do you really want to delete this weight goal?")) {
        const wgId = this.$javalin.pathParams["weight-goal-id"];
        const url = `/api/weightGoals/${wgId}`;
        axios.delete(url)
            .then(() => {
              alert("Weight Goal deleted");
              // Redirect to the weight goals endpoint
              window.location.href = '/weightGoals';
            })
            .catch(error => {
              console.log(error);
              alert("Error deleting Weight Goal");
            });
      }
    }
  }
});
</script>
