<template id="activity-profile">
  <app-layout>
    <div v-if="!activity">
      <p> We're sorry, we were not able to retrieve this activity.</p>
      <p> View <a :href="'/activities'">all activities</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="activity">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Activity Profile</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateActivity()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteActivity()">
              <i class="fas fa-trash" aria-hidden="true"></i>
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
    activity: null
  }),
  created: function () {
    const actId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${actId}`
    axios.get(url)
        .then(res => this.activity = res.data)
        .catch(() => alert("Error while fetching activity" + actId));
  },
  methods: {
    updateActivity: function () {
      // Add your logic to update the activity here
      alert("Activity updated!");
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