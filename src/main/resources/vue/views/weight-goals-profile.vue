<template id="weight-goals-profile">
  <app-layout>
    <div v-if="noWeightGoal">
      <p>We're sorry, we were not able to retrieve this weight goal.</p>
      <p>View <a :href="'/weightGoals'">all weight goals</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noWeightGoal">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Weight Goal Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateWeightGoal()"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteWeightGoal()"
            >
              <i class="fas fa-trash" aria-hidden="true" style="color: #08a29e;"></i>
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
      <div class="card-footer text-center">
        <div v-if="weightGoal">
          <br>
          <a :href="`/weightGoals/${weightGoal.id}/userWeight`">View Current Weight</a>
          <br>
          <br>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("weight-goals-profile", {
  template: "#weight-goals-profile",
  data: () => ({
    weightGoal: null,
    noWeightGoal: false
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
  },
  methods: {
    updateWeightGoal: function () {
      const wgId = this.$javalin.pathParams["weight-goal-id"];
      const url = `/api/weightGoals/${wgId}`;
      // Modify the data structure to match your weight goal properties
      const updatedWeightGoal = {
        id: this.weightGoal.id,
        type: this.weightGoal.type,
        startingWeight: this.weightGoal.startingWeight,
        startingWeightTimestamp: this.weightGoal.startingWeightTimestamp,
        targetWeight: this.weightGoal.targetWeight,
        weeklyGoal: this.weightGoal.weeklyGoal,
        deadline: this.weightGoal.deadline,
        userId: this.weightGoal.userId
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
