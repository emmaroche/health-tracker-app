<template id="user-weight-goals-overview">
  <app-layout>
    <div>
      <h3>Weight Goals List</h3>
      <ul>
        <li v-for="weightGoal in weightGoals">
          {{ weightGoal.id }}: {{ weightGoal.type }} (Starting Weight: {{ weightGoal.startingWeight }}, Current Weight: {{ weightGoal.currentWeight }}, Target Weight: {{ weightGoal.targetWeight }}, Weekly Goal: {{ weightGoal.weeklyGoal }}, Deadline: {{ weightGoal.deadline }}, User ID: {{ weightGoal.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-weight-goals-overview",{
  template: "#user-weight-goals-overview",
  data: () => ({
    weightGoals: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/weightGoals`)
        .then(res => this.weightGoals = res.data)
        .catch(() => alert("Error while fetching weight goals"));
  }
});
</script>