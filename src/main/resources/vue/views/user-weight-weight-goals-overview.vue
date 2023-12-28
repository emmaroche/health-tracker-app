<template id="user-weight-weight-goals-overview">
  <app-layout>
    <div class="mt-4">
      <h5>Weight Entries</h5>
      <ul>
        <li v-for="currentWeight in userWeight">
          Current Weight: {{ currentWeight.currentWeight }} kg (Timestamp: {{ currentWeight.currentWeightTimestamp }}, Weight ID: {{ currentWeight.weightGoalId }}, User ID: {{ currentWeight.userId }})
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
        .catch(() => alert("We couldn't find any current weight at the moment. Feel free to add a new current weight, wait a moment, or refresh the page to check again"));
  }
});
</script>
