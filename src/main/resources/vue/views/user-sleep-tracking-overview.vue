<template id="user-sleep-tracking-overview">
  <app-layout>
    <div>
      <h3>Sleep Entries List</h3>
      <ul>
        <li v-for="sleepEntry in sleepTracking">
          {{ sleepEntry.id }}: {{ sleepEntry.quality }} (Duration: {{ sleepEntry.duration }} minutes, Notes: {{ sleepEntry.notes }}, Date: {{ sleepEntry.date }}, User ID: {{ sleepEntry.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-sleep-tracking-overview", {
  template: "#user-sleep-tracking-overview",
  data: () => ({
    sleepTracking: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/sleepTracking`)
        .then(res => this.sleepTracking = res.data)
        .catch(() => alert("Error while fetching sleep entries"));
  }
});
</script>
