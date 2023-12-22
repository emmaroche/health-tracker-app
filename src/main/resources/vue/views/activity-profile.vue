<template id="activity-profile">
  <app-layout>
    <div v-if="noActivity">
      <p>We're sorry, we were not able to retrieve this activity.</p>
      <p>View <a :href="'/activities'">all activities</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noActivity">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Activity Profile</div>
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
          <div class="form-group">
            <label class="col-form-label">Activity ID:</label>
            <input class="form-control" v-model="activity.id" name="id" type="number" readonly/>
          </div>
          <div class="form-group">
            <label class="col-form-label">Description:</label>
            <input class="form-control" v-model="activity.description" name="description" type="text"/>
          </div>
          <div class="form-group">
            <label class="col-form-label">Duration (in hours):</label>
            <input class="form-control" v-model="activity.duration" name="duration" type="number"/>
          </div>
          <div class="form-group">
            <label class="col-form-label">Calories:</label>
            <input class="form-control" v-model="activity.calories" name="calories" type="number"/>
          </div>
          <div class="form-group">
            <label class="col-form-label">Start Time:</label>
            <input class="form-control" v-model="activity.started" name="started" type="text"/>
          </div>
          <div class="form-group">
            <label class="col-form-label">User ID:</label>
            <input class="form-control" v-model="activity.userId" name="userId" type="number"/>
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("activity-profile", {
  template: "#activity-profile",
  data: () => ({
    activity: null,
    noActivity: false
  }),
  created: function () {
    const actId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${actId}`
    axios.get(url)
        .then(res => this.activity = res.data)
        .catch(() => alert("Error while fetching activity" + actId));
         this.activity = true;
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
        userId: this.activity.userId
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