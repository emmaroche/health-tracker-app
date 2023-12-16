<template id="weight-goals-user-weight-overview">
  <app-layout>
    <div>
      <h3>User Weight Data</h3>
      <ul>
        <li v-for="currentWeight in userWeight">
    Your weight was {{ currentWeight.currentWeight }} kg on {{ currentWeight.currentWeightTimestamp }}
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("weight-goals-user-weight-overview", {
  template: "#weight-goals-user-weight-overview",
  data: () => ({
    userWeight: [],
  }),
  created() {

    const wgID = this.$javalin.pathParams["weight-goals-id"];

    // Fetch User Weight Data
    axios.get(`/api/weightGoals/${wgID}/userWeight`)
        .then(res => this.userWeight = res.data)
        .catch(() => alert("Error while fetching user weight data"));
  },
});
</script>
