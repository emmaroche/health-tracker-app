<template id="nutrition-goals-profile">
  <app-layout>
    <div v-if="noNutritionGoal">
      <p>We're sorry, we were not able to retrieve this Nutrition Goal.</p>
      <p>View <a :href="'/nutritionGoals'">all nutrition goals</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noNutritionGoal">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;"> Nutrition Goal Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateNutritionGoal"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteNutritionGoal"
            >
              <i class="fas fa-trash" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-description">Type</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.type" name="type" type="text" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Protein Goal</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.proteinGoal" name="proteinGoal" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-calories">Fibre Goal</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.fibreGoal" name="fibreGoal" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Calorie Goal</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.calorieGoal" name="calorieGoal" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Carbs Goal</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.carbsGoal" name="carbsGoal" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-deadline">Fat Goal</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.fatGoal" name="fatGoal" type="number" />
        </div>

        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-userId">User ID</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.userId" name="userId" type="number" />
        </div>
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-fitnessId">Fitness ID</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.fitnessId" name="fitnessId" type="number" />
        </div>
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-weightId">Weight ID</span>
          </div>
          <input class="form-control" v-model="nutritionGoal.weightId" name="weightId" type="number" />
        </div>
      </div>
    </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 150px;
}
</style>

<script>
app.component("nutrition-goals-profile", {
  template: "#nutrition-goals-profile",
  data: () => ({
    nutritionGoal: null,
    noNutritionGoal: false
  }),
  created: function () {
    const ngId = this.$javalin.pathParams["nutrition-goal-id"];
    const url = `/api/nutritionGoals/${ngId}`;
    axios.get(url)
        .then(res => this.nutritionGoal = res.data)
        .catch(() => {
          console.error("Error while fetching nutrition goal: " + ngId);
          this.noNutritionGoal = true;
        });
  },
  methods: {
    updateNutritionGoal: function () {
      const ngId = this.$javalin.pathParams["nutrition-goal-id"];
      const url = `/api/nutritionGoals/${ngId}`;
      // Modify the data structure to match your nutrition goal properties
      const updatedNutritionGoal = {
        id: this.nutritionGoal.id,
        type: this.nutritionGoal.type,
        proteinGoal: this.nutritionGoal.proteinGoal,
        fibreGoal: this.nutritionGoal.fibreGoal,
        calorieGoal: this.nutritionGoal.calorieGoal,
        carbsGoal: this.nutritionGoal.carbsGoal,
        fatGoal: this.nutritionGoal.fatGoal,
        userId: this.nutritionGoal.userId,
        fitnessId: this.nutritionGoal.fitnessId,
        weightId: this.nutritionGoal.weightId,
      };

      axios.patch(url, updatedNutritionGoal)
          .then(response => {
            const responseData = response.data;
            Object.assign(this.nutritionGoal, responseData);
            alert("Nutrition Goal updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating Nutrition Goal");
          });
    },
    deleteNutritionGoal: function () {
      if (confirm("Do you really want to delete this nutrition goal?")) {
        const ngId = this.$javalin.pathParams["nutrition-goal-id"];
        const url = `/api/nutritionGoals/${ngId}`;
        axios.delete(url)
            .then(() => {
              alert("Nutrition Goal deleted");
              // Redirect to the nutrition goals endpoint
              window.location.href = '/nutritionGoals';
            })
            .catch(error => {
              console.log(error);
              alert("Error deleting Nutrition Goal");
            });
      }
    }
  }
});
</script>
