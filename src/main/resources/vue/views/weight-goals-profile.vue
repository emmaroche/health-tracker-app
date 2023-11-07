<template id="weight-goals-profile">
  <app-layout>
    <div>
      <form v-if="weightGoal">
        <label class="col-form-label">Goal ID:</label>
        <input class="form-control" v-model="weightGoal.id" name="id" type="number" readonly/><br>
        <label class="col-form-label">Type:</label>
        <input class="form-control" v-model="weightGoal.type" name="type" type="text"/><br>
        <label class="col-form-label">Starting Weight:</label>
        <input class="form-control" v-model="weightGoal.startingWeight" name="startingWeight" type="number"/><br>
        <label class="col-form-label">Starting Weight Timestamp:</label>
        <input class="form-control" v-model="weightGoal.startingWeightTimestamp" name="startingWeightTimestamp" type="datetime-local"/><br>
        <label class="col-form-label">Current Weight:</label>
        <input class="form-control" v-model="weightGoal.currentWeight" name="currentWeight" type="number"/><br>
        <label class="col-form-label">Target Weight:</label>
        <input class="form-control" v-model="weightGoal.targetWeight" name="targetWeight" type="number"/><br>
        <label class="col-form-label">Weekly Goal:</label>
        <input class="form-control" v-model="weightGoal.weeklyGoal" name="weeklyGoal" type="number"/><br>
        <label class="col-form-label">Deadline:</label>
        <input class="form-control" v-model="weightGoal.deadline" name="deadline" type="datetime-local"/><br>
        <label class="col-form-label">User ID:</label>
        <input class="form-control" v-model="weightGoal.userId" name="userId" type="number"/><br>
      </form>
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
    console.log("Fetching weight goal data from URL: " + url);
    axios.get(url)
        .then(res => {
          console.log("Received data:", res.data);
          this.weightGoal = res.data;
        })
        .catch(error => {
          console.error("Error while fetching weight goal: " + wgId, error);
          alert("Error while fetching weight goal" + wgId);
        });
  }

});
</script>
