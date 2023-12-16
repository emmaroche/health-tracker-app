<template id="nutrition-goals-profile">
  <app-layout>
    <div v-if="noNutritionGoal">
      <p>We're sorry, we were not able to retrieve this Nutrition Goal.</p>
      <p>View <a :href="'/nutritionGoals'">all nutrition goals</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noNutritionGoal">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Nutrition Goal Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link"
                @click="updateNutritionGoal()"
            >
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteNutritionGoal()"
            >
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="form-group">
            <label class="col-form-label">Goal ID:</label>
            <input class="form-control" v-model="nutritionGoal.id" name="id" type="number" readonly />
          </div>
          <div class="form-group">
            <label class="col-form-label">Type:</label>
            <input class="form-control" v-model="nutritionGoal.type" name="type" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Protein Goal:</label>
            <input class="form-control" v-model="nutritionGoal.proteinGoal" name="proteinGoal" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Fibre Goal:</label>
            <input class="form-control" v-model="nutritionGoal.fibreGoal" name="fibreGoal" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Calorie Goal:</label>
            <input class="form-control" v-model="nutritionGoal.calorieGoal" name="calorieGoal" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Carbs Goal:</label>
            <input class="form-control" v-model="nutritionGoal.carbsGoal" name="carbsGoal" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Fat Goal:</label>
            <input class="form-control" v-model="nutritionGoal.fatGoal" name="fatGoal" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">User ID:</label>
            <input class="form-control" v-model="nutritionGoal.userId" name="userId" type="number" />
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

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
        userId: this.nutritionGoal.userId
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
              window.location.href = '/nutrition-goals';
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
