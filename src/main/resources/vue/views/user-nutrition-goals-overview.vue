<template id="user-nutrition-goals-overview">
  <app-layout>
    <div>
      <h3>Nutrition Goals List</h3>
      <ul>
        <li v-for="nutritionGoal in nutritionGoals">
          {{ nutritionGoal.id }}: {{ nutritionGoal.type }} (Protein Goal: {{ nutritionGoal.proteinGoal }}, Fibre Goal: {{ nutritionGoal.fibreGoal }}, Calorie Goal: {{ nutritionGoal.calorieGoal }}, Carbs Goal: {{ nutritionGoal.carbsGoal }}, Fat Goal: {{ nutritionGoal.fatGoal }}, User ID: {{ nutritionGoal.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-nutrition-goals-overview",{
  template: "#user-nutrition-goals-overview",
  data: () => ({
    nutritionGoals: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/nutritionGoals`)
        .then(res => this.nutritionGoals = res.data)
        .catch(() => alert("Error while fetching nutrition goals"));
  }
});
</script>
