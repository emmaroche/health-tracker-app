<template id="fitness-goals-activity-overview">
  <app-layout>
    <div class="mt-4">
      <h5>Activities list </h5>
      <ul>
        <li v-for="activity in activities" class="mt-3">
          {{activity.description}} for {{activity.duration}} hour to burn {{activity.calories}} calories
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
  app.component("fitness-goals-activity-overview",{
  template: "#fitness-goals-activity-overview",
  data: () => ({
  activities: [],
}),
  created() {
  const fitnessId = this.$javalin.pathParams["fitness-goal-id"];
  axios.get(`/api/fitnessGoals/${fitnessId}/activities`)
  .then(res => this.activities = res.data)
  .catch(() => alert("We couldn't find any activities at the moment. Feel free to add a new activity, wait a moment, or refresh the page to check again"));
}
});
</script>