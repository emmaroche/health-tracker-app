<template id="weight-goal-nutrition-goal-overview">
  <app-layout>
    <div class="mt-4">
      <h3>Nutrition Goals List</h3>
      <ul>
        <li v-for="nutritionGoal in nutritionGoals" class="mt-3">
          {{ nutritionGoal.type }} (Protein Goal: {{ nutritionGoal.proteinGoal }} grams, Fibre Goal: {{ nutritionGoal.fibreGoal }} grams, Calorie Goal: {{ nutritionGoal.calorieGoal }} calories, Carbs Goal: {{ nutritionGoal.carbsGoal }} grams, Fat Goal: {{ nutritionGoal.fatGoal }} grams, User ID: {{ nutritionGoal.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("weight-goal-nutrition-goal-overview",{
  template: "#weight-goal-nutrition-goal-overview",
  data: () => ({
    nutritionGoals: [],
  }),
  created() {
    const weightId = this.$javalin.pathParams["weight-goal-id"];
    axios.get(`/api/weightGoals/${weightId}/nutritionGoals`)
        .then(res => this.nutritionGoals = res.data)
        .catch(() => alert("Error while fetching nutrition goals"));
  }
});
</script>
