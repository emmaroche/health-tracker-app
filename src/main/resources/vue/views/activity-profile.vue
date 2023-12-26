<template id="activity-profile">
  <app-layout>
    <div v-if="noActivity">
      <p>We're sorry, we were not able to retrieve this activity.</p>
      <p>View <a :href="'/activities'">all activities</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noActivity">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;"> Activity Profile</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link mr-2"
                    @click="updateActivity()">
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteActivity()">
              <i class="fas fa-trash" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
<!--          <div class="input-group mb-3">-->
<!--            <div class="input-group-prepend">-->
<!--              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-id">Activity ID</span>-->
<!--            </div>-->
<!--            <input type="number" class="form-control" v-model="activity.id" name="id" readonly placeholder="Id"/>-->
<!--          </div>-->
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="activity.description" name="description" placeholder="Description"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Duration (hours)</span>
            </div>
            <input type="number" class="form-control" v-model="activity.duration" name="duration"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-calories">Calories Burnt</span>
            </div>
            <input type="number" class="form-control" v-model="activity.calories" name="calories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Date & Time</span>
            </div>
            <input type="text" class="form-control" v-model="activity.started" name="started"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-userId">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="activity.userId" name="userId"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-fitnessGoalId">Fitness Goal ID</span>
            </div>
            <input type="number" class="form-control" v-model="activity.fitnessId" name="fitnessGoalId" readonly placeholder="Fitness Goal ID"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <p v-if="weightGoals.length === 0">
          No goals associated yet
        </p>
        <p v-else>
          View the weight goal associated with this activity to help enhance your goal progress:
        </p>
        <div class="card-footer text-center">
          <div v-if="activity">
            <div class="btn-group-vertical" role="group" aria-label="Fitness Actions">
              <a :href="`/activities/${activity.id}/weightGoals`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-weight"></i> View Weight Goals
              </a>
            </div>
          </div>
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
app.component("activity-profile", {
  template: "#activity-profile",
  data: () => ({
    activity: null,
    noActivity: false,
    weightGoals: [],
  }),
  created: function () {
    const actId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${actId}`

    axios.get(url)
        .then(res => {
          console.log("Activity Response:", res.data);
          this.activity = res.data;
        })
        .catch(error => {
          console.error("Error while fetching activity:", error);
          this.noActivity = true;
        });

    axios.get(url + `/weightGoals`)
        .then(res => this.weightGoals = res.data)
        .catch(error => {
          console.log("No weight goals added yet: " + error)
        })
  },
  methods: {
    updateActivity: function () {
      const actId = this.$javalin.pathParams["activity-id"];
      const url = `/api/activities/${actId}`;
      // Modify the data structure to match your activity properties
      const updatedActivity = {
        id: this.activity.id,
        description: this.activity.description,
        duration: this.activity.duration,
        calories: this.activity.calories,
        started: this.activity.started,
        userId: this.activity.userId,
        fitnessId: this.activity.fitnessId,
        actId: this.activity.actId
      };

      axios.patch(url, updatedActivity)
          .then(response => {
            const responseData = response.data;
            Object.assign(this.activity, responseData);
            alert("Activity updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating Activity");
          });
    },
    deleteActivity: function () {
      if (confirm("Do you really want to delete this activity?")) {
        // Add your logic to delete the activity here
        alert("Activity deleted");
        // Redirect to the activities endpoint
        window.location.href = '/activities';
      }
    }
  }
});
</script>