<template id="sleep-tracking-mood-tracking-overview">
  <app-layout>
    <div class="mt-4">
      <h5>Mood Entries List</h5>
      <ul>
        <li v-for="moodEntry in moodTracking">
         {{ moodEntry.mood }} (Rating: {{ moodEntry.rating }}, Notes: {{ moodEntry.notes }}, Date: {{ moodEntry.date }}, User ID: {{ moodEntry.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
  app.component("sleep-tracking-mood-tracking-overview", {
  template: "#sleep-tracking-mood-tracking-overview",
  data: () => ({
  moodTracking: [],
}),
  created() {
  const sleepId = this.$javalin.pathParams["sleep-tracking-id"];
  axios.get(`/api/sleepTracking/${sleepId}/moodTracking`)
  .then(res => this.moodTracking = res.data)
  .catch(() => alert("Error while fetching mood entries"));
}
});
</script>
