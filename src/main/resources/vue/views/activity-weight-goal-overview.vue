<template id="activity-weight-goal-overview">
  <app-layout>
    <div class="mt-4">
      <h5>Weight Goals List</h5>
      <ul>
        <li v-for="weightGoal in weightGoals" class="mt-3">
          <strong>Goal:</strong> {{ weightGoal.type }} (Starting Weight: {{ weightGoal.startingWeight }}, Target Weight: {{ weightGoal.targetWeight }}, Weekly Goal: gain {{ weightGoal.weeklyGoal }}, Deadline: {{ weightGoal.deadline }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("activity-weight-goal-overview", {
  template: "#activity-weight-goal-overview",
  data: () => ({
    weightGoals: [],
  }),
  created() {
    const actId = this.$javalin.pathParams["activity-id"];
    axios.get(`/api/activities/${actId}/weightGoals`)
        .then(res => this.weightGoals = res.data)
        .catch(() => alert("We couldn't find any weight goals at the moment. Feel free to add a new weight goal, wait a moment, or refresh the page to check again"));
  }
});
</script>
