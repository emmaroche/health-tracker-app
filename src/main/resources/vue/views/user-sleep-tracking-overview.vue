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
        .catch(() => alert("We couldn't find any sleep entries at the moment. Feel free to add a new sleep entry, wait a moment, or refresh the page to check again"));
  }
});
</script>
