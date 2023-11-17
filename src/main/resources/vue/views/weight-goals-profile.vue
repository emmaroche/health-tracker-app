<template id="weight-goals-profile">
  <app-layout>
    <div v-if="!weightGoal">
      <p>We're sorry, we were not able to retrieve this weight goal.</p>
      <p>View <a :href="'/weight-goals'">all weight goals</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="weightGoal">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Weight Goal Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link"
                @click="updateWeightGoal()"
            >
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteWeightGoal()"
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
            <input class="form-control" v-model="weightGoal.id" name="id" type="number" readonly />
          </div>
          <div class="form-group">
            <label class="col-form-label">Type:</label>
            <input class="form-control" v-model="weightGoal.type" name="type" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Starting Weight:</label>
            <input class="form-control" v-model="weightGoal.startingWeight" name="startingWeight" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Starting Weight Timestamp:</label>
            <input class="form-control" v-model="weightGoal.startingWeightTimestamp" name="startingWeightTimestamp" type="datetime-local" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Current Weight:</label>
            <input class="form-control" v-model="weightGoal.currentWeight" name="currentWeight" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Target Weight:</label>
            <input class="form-control" v-model="weightGoal.targetWeight" name="targetWeight" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Weekly Goal:</label>
            <input class="form-control" v-model="weightGoal.weeklyGoal" name="weeklyGoal" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Deadline:</label>
            <input class="form-control" v-model="weightGoal.deadline" name="deadline" type="datetime-local" />
          </div>
          <div class="form-group">
            <label class="col-form-label">User ID:</label>
            <input class="form-control" v-model="weightGoal.userId" name="userId" type="number" />
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("weight-goals-profile", {
  template: "#weight-goals-profile",
  data: () => ({
    weightGoal: null
  }),
  created: function () {
    const wgId = this.$javalin.pathParams["weight-goal-id"];
    const url = `/api/weightGoals/${wgId}`;
    axios.get(url)
        .then(res => this.weightGoal = res.data)
        .catch(() => {
          console.error("Error while fetching weight goal: " + wgId);
          this.weightGoal = null;
        });
  },
  methods: {
    updateWeightGoal: function () {
      const wgId = this.$javalin.pathParams["weight-goal-id"];
      const url = `/api/weightGoals/${wgId}`;
      axios.patch(url, this.weightGoal)
          .then(response => {
            this.weightGoal = response.data;
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
              window.location.href = '/weight-goals';
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
