<template id="user-weight-weight-goals-overview">
  <app-layout>
    <div class="mt-4">
      <h5>Weight Entries</h5>
      <ul>
        <li v-for="currentWeight in userWeight">
          Current Weight: {{ currentWeight.currentWeight }} (Timestamp: {{ currentWeight.currentWeightTimestamp }}, Weight ID: {{ currentWeight.weightGoalId }}, User ID: {{ currentWeight.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-weight-weight-goals-overview", {
  template: "#user-weight-weight-goals-overview",
  data: () => ({
    userWeight: [],
  }),
  created() {
    const weightGoalId = this.$javalin.pathParams["weight-goal-id"];
    axios.get(`/api/weightGoals/${weightGoalId}/userWeight`)
        .then(res => this.userWeight = res.data)
        .catch(() => alert("Error while fetching weight entries"));
  }
});
</script>
