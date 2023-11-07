<template id="weight-goals-overview">
  <app-layout>
    <div>
      <div>
        <ul class="weight-goals-overview-list">
          <li v-for="weightGoal in weightGoals">
            <a :href="`/weightGoals/${weightGoal.id}`">{{ weightGoal.type }} (Starting Weight: {{ weightGoal.startingWeight }}, Current Weight: {{ weightGoal.currentWeight }}, Target Weight: {{ weightGoal.targetWeight }}, Weekly Goal: {{ weightGoal.weeklyGoal }}, Deadline: {{ weightGoal.deadline }}, User ID: {{ weightGoal.userId }})</a>
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("weight-goals-overview", {
  template: "#weight-goals-overview",
  data: () => ({
    weightGoals: [],
  }),
  created() {
    this.fetchWeightGoals();
  },
  methods: {
    fetchWeightGoals: function () {
      axios.get("/api/weightGoals")
          .then(res => this.weightGoals = res.data)
          .catch(() => alert("Error while fetching weight goals"));
    }
  }
});
</script>