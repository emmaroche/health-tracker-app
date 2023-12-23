<template id="fitness-goals-nutrition-goal-overview">
  <app-layout>
    <div class="mt-4">
      <h5>Nutrition Goals List</h5>
      <ul>
        <li v-for="nutritionGoal in nutritionGoals" class="mt-3">
          {{ nutritionGoal.type }} (Protein Goal: {{ nutritionGoal.proteinGoal }} grams, Fibre Goal: {{ nutritionGoal.fibreGoal }} grams, Calorie Goal: {{ nutritionGoal.calorieGoal }} calories, Carbs Goal: {{ nutritionGoal.carbsGoal }} grams, Fat Goal: {{ nutritionGoal.fatGoal }} grams, User ID: {{ nutritionGoal.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
  app.component("fitness-goals-nutrition-goal-overview",{
  template: "#fitness-goals-nutrition-goal-overview",
  data: () => ({
  nutritionGoals: [],
}),
  created() {
    const fitnessId = this.$javalin.pathParams["fitness-goal-id"];
  axios.get(`/api/fitnessGoals/${fitnessId}/nutritionGoals`)
  .then(res => this.nutritionGoals = res.data)
  .catch(() => alert("Error while fetching nutrition goals"));
}
});
</script>