<template id="user-mood-tracking-overview">
  <app-layout>
    <div>
      <h3>Mood Entries List</h3>
      <ul>
        <li v-for="moodEntry in moodTracking">
          {{ moodEntry.id }}: {{ moodEntry.mood }} (Rating: {{ moodEntry.rating }}, Notes: {{ moodEntry.notes }}, Date: {{ moodEntry.date }}, User ID: {{ moodEntry.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-mood-tracking-overview", {
  template: "#user-mood-tracking-overview",
  data: () => ({
    moodTracking: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/moodTracking`)
        .then(res => this.moodTracking = res.data)
        .catch(() => alert("Error while fetching mood entries"));
  }
});
</script>
