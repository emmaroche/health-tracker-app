<template id="weight-goals-user-weight-profile">
  <app-layout>
    <div v-if="noCurrentWeightGoal">
      <p>We're sorry, we were not able to retrieve your current weight.</p>
      <p>View <a :href="'/userWeight'">all weight entries</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="userWeight">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">Current Weight</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateUserWeight"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteUserWeight"
            >
              <i class="fas fa-trash" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Current Weight (kg)</span>
            </div>
            <input class="form-control" v-model="userWeight.currentWeight" name="currentWeight" type="number" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Current Weight Date</span>
            </div>
            <input class="form-control" v-model="currentWeightTimestamp" name="currentWeightTimestamp" type="date" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-userId">User ID</span>
            </div>
            <input class="form-control" v-model="userWeight.userId" name="userId" type="number" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight-goal-actId">Weight Goal ID</span>
            </div>
            <input type="text" class="form-control" v-model="userWeight.weightGoalId" name="weightGoalId" placeholder="Weight Goal ID"/>
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 230px;
}
</style>

<script>
app.component("weight-goals-user-weight-profile", {
  template: "#weight-goals-user-weight-profile",
  data: () => ({
    userWeight: null,
    noCurrentWeightGoal: false,
    currentWeightTimestamp: null,
  }),
  created: function () {
    const userWeightId = this.$javalin.pathParams["user-weight-id"];
    const url = `/api/userWeight/${userWeightId}`;
    axios.get(url)
        .then(res => {
          this.userWeight = res.data;

          // Format the current weight date
          this.currentWeightTimestamp = formatDate(this.userWeight.currentWeightTimestamp);
        })
        .catch(() => {
          console.error("Error while fetching user weight: " + userWeightId);
          this.noCurrentWeightGoal = true;
        });
  },
  methods: {
    updateUserWeight: function () {
      const userWeightId = this.$javalin.pathParams["user-weight-id"];
      const url = `/api/userWeight/${userWeightId}`;

      const updatedUserWeight = {
        id: this.userWeight.id,
        currentWeight: this.userWeight.currentWeight,
        currentWeightTimestamp: formatDateISO(this.currentWeightTimestamp),
        userId: this.userWeight.userId,
        weightGoalId: this.userWeight.weightGoalId
      };

      axios.patch(url, updatedUserWeight)
          .then(response => {
            const responseData = response.data;
            Object.assign(this.userWeight, responseData);
            alert("User Weight updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating User Weight");
          });
    },
    deleteUserWeight: function () {
      if (confirm("Do you really want to delete this user weight entry?")) {
        const userWeightId = this.$javalin.pathParams["user-weight-id"];
        const url = `/api/userWeight/${userWeightId}`;
        axios.delete(url)
            .then(() => {
              alert("User Weight deleted");
              // Redirect to the user weight entries endpoint
              window.location.href = '/userWeight';
            })
            .catch(error => {
              console.log(error);
              alert("Error deleting User Weight");
            });
      }
    }
  }
});

// Helper function to format date
function formatDate(date) {
  const formattedDate = new Date(date);
  return formattedDate.toISOString().split('T')[0];
}

// Helper function to format date as ISO string
function formatDateISO(date) {
  return new Date(date).toISOString();
}
</script>
