<template id="nutrition-goals-overview">
  <app-layout>
    <div class="card bg-light mt-4 mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">Nutrition Goals</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-sm" @click="hideForm = !hideForm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-plus" aria-hidden="true"></i> Add
            </button>
          </div>
        </div>
      </div>
      <div class="col-12 ml-2 mb-3 mt-3" style="font-weight: 400;">Effortlessly manage your nutrition goals with our Nutrition Goals feature. Stay on track with your nutritional journey!  </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addNutritionGoal">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-nutrition-goal-type">Type</span>
            </div>
            <input type="text" class="form-control" v-model="formData.type" name="type" placeholder="Type"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-nutrition-goal-protein">Protein Goal (grams)</span>
            </div>
            <input type="number" class="form-control" v-model="formData.proteinGoal" name="proteinGoal" placeholder="Protein Goal"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-nutrition-goal-fibre">Fibre Goal (grams)</span>
            </div>
            <input type="number" class="form-control" v-model="formData.fibreGoal" name="fibreGoal" placeholder="Fibre Goal"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-nutrition-goal-calorie">Calorie Goal</span>
            </div>
            <input type="number" class="form-control" v-model="formData.calorieGoal" name="calorieGoal" placeholder="Calorie Goal"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-nutrition-goal-carbs">Carbs Goal (grams)</span>
            </div>
            <input type="number" class="form-control" v-model="formData.carbsGoal" name="carbsGoal" placeholder="Carbs Goal"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-nutrition-goal-fat">Fat Goal (grams)</span>
            </div>
            <input type="number" class="form-control" v-model="formData.fatGoal" name="fatGoal" placeholder="Fat Goal"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-nutrition-goal-userId">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-fitnessId">Fitness ID</span>
            </div>
            <input class="form-control" v-model="formData.fitnessId" name="fitnessId" type="number" placeholder="Fitness ID" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-weightId">Weight ID</span>
            </div>
            <input class="form-control" v-model="formData.weightId" name="weightId" type="number" placeholder="Weight ID" />
          </div>
        </form>
        <button rel="tooltip" title="Add Nutrition Goal" class="btn btn-info btn-sm mt-3" @click="addNutritionGoal" style="background-color: #08a29e; border-color: #08a29e;">
          <i class="fa fa-plus" aria-hidden="true"></i> Add Nutrition Goal
        </button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="col-6 mb-3" style="font-weight: 600;">Current Nutrition Goals</div>
      <div class="list-group-item d-flex align-items-start" v-for="(nutritionGoal, index) in nutritionGoals" :key="index">
        <div class="mr-auto p-2">
          <p><strong>ID:</strong> {{ nutritionGoal.id }}</p>
          <p><strong>Type:</strong> {{ nutritionGoal.type }}</p>
          <p><strong>Protein Goal:</strong> {{ nutritionGoal.proteinGoal }} g</p>
          <p><strong>Fibre Goal:</strong> {{ nutritionGoal.fibreGoal }} g</p>
          <p><strong>Calorie Goal:</strong> {{ nutritionGoal.calorieGoal }} calories</p>
          <p><strong>Carbs Goal:</strong> {{ nutritionGoal.carbsGoal }} g</p>
          <p><strong>Fat Goal:</strong> {{ nutritionGoal.fatGoal }} g</p>
          <p><strong>User ID:</strong> {{ nutritionGoal.userId }}</p>
          <p><strong>Fitness ID:</strong> {{ nutritionGoal.fitnessId }}</p>
          <p><strong>Weight ID:</strong> {{ nutritionGoal.weightId }}</p>
          <a :href="`/nutritionGoals/${nutritionGoal.id}`" style="color: #08a29e;">
            View Details
          </a>
        </div>
        <div class="p-2">
          <div class="btn-group d-flex" role="group">
            <a :href="`/nutritionGoals/${nutritionGoal.id}`" class="btn btn-info btn-sm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </a>
            <div class="mr-2"></div> <!-- Added margin-right for spacing -->
            <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm" @click="deleteNutritionGoal(nutritionGoal, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 180px;
}
</style>

<script>
app.component("nutrition-goals-overview", {
  template: "#nutrition-goals-overview",
  data: () => ({
    nutritionGoals: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchNutritionGoals();
  },
  methods: {
    fetchNutritionGoals: function () {
      axios.get("/api/nutritionGoals")
          .then(res => this.nutritionGoals = res.data)
          .catch(() => alert("We couldn't find any nutrition goals at the moment. Feel free to add a new nutrition goal, wait a moment, or refresh the page to check again"));
    },
    deleteNutritionGoal: function (nutritionGoal, index) {
      if (confirm('Are you sure you want to delete this nutrition goal? This action cannot be undone.', 'Warning')) {
        const nutritionGoalId = nutritionGoal.id;
        const url = `/api/nutritionGoals/${nutritionGoalId}`;
        axios.delete(url)
            .then(response =>
                this.nutritionGoals.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error);
            });
      }
    },
    addNutritionGoal: function () {
      const url = "/api/nutritionGoals";
      axios.post(url, this.formData)
          .then(response => {
            this.nutritionGoals.push(response.data);
            this.hideForm = true;
            // Reset form data after adding nutrition goal
            this.formData = {
              type: '',
              proteinGoal: null,
              fibreGoal: null,
              calorieGoal: null,
              carbsGoal: null,
              fatGoal: null,
              userId: null,
            };
          })
          .catch(error => {
            console.log(error);
          });
    },
  }
});
</script>
