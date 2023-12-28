<template id="user-activity-overview">
  <app-layout>
  <div>
    <h3>Activities list </h3>
    <ul>
      <li v-for="activity in activities">
        {{activity.id}}: {{activity.description}} for {{activity.duration}} minutes
      </li>
    </ul>
  </div>
  </app-layout>
</template>

<script>
app.component("user-activity-overview",{
  template: "#user-activity-overview",
  data: () => ({
    activities: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/activities`)
        .then(res => this.activities = res.data)
        .catch(() => alert("We couldn't find any activities at the moment. Feel free to add a new activity, wait a moment, or refresh the page to check again"));
  }
});
</script>