<template id="activity-overview">
  <app-layout>
    <div class="card bg-light mt-4 mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">
            Activities
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-sm" @click="hideForm = !hideForm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-plus" aria-hidden="true"></i> Add
            </button>
          </div>
        </div>
      </div>
      <div class="col-12 ml-2 mb-3 mt-3" style="font-weight: 400;"> Elevate your fitness journey with our Activities feature.
        Stay motivated and keep moving forward! </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addActivity">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="formData.description" name="description" placeholder="Description"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Duration</span>
            </div>
            <input type="text" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-calories">Calories Burnt</span>
            </div>
            <input type="text" class="form-control" v-model="formData.calories" name="calories" placeholder="Calories Burnt"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Date & Time</span>
            </div>
            <input type="text" class="form-control" v-model="formData.started" name="started" placeholder="Date & Time"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-userId">User ID</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>

          <button rel="tooltip" title="Add Activity" class="btn btn-info btn-sm mt-3" @click="addActivity" style="background-color: #08a29e; border-color: #08a29e;">
            <i class="fa fa-plus" aria-hidden="true"></i> Add Activity
          </button>
        </form>
      </div>

    </div>
    <div class="list-group list-group-flush">
      <div class="col-6 mb-3" style="font-weight: 600;">Current Activities Goals</div>
      <div class="list-group-item d-flex align-items-start"
           v-for="(activity, index) in activities" :key="index">
        <div class="mr-auto p-2">
          <span>
            <a :href="`/activities/${activity.id}`" style="color: #08a29e;">
              {{ activity.description }} (Duration: {{ activity.duration }}, Calories Burnt: {{ activity.calories }}, Date & Time of Activity: {{ activity.started }}, User ID: {{ activity.userId }})
            </a>
          </span>
        </div>
        <div class="p-2">
        <div class="btn-group d-flex" role="group">
          <a :href="`/activities/${activity.id}`" class="btn btn-info btn-sm mr-2" style="background-color: #08a29e; border-color: #08a29e;">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm" @click="deleteActivity(activity, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
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
app.component("activity-overview", {
  template: "#activity-overview",
  data: () => ({
    activities: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchActivities();
  },
  methods: {
    fetchActivities: function () {
      axios.get("/api/activities")
          .then(res => this.activities = res.data)
          .catch(() => alert("Error while fetching activities"));
    },
    deleteActivity: function (activity, index) {
      if (confirm('Are you sure you want to delete this activity? This action cannot be undone.', 'Warning')) {
        const activityId = activity.id;
        const url = `/api/activities/${activityId}`;
        axios.delete(url)
            .then(response =>
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error);
            });
      }
    },
    addActivity: function () {
      const url = "/api/activities";
      axios.post(url, this.formData)
          .then(response => {
            this.activities.push(response.data);
            this.hideForm = true;
            // Reset form data after adding activity
            this.formData = {};
          })
          .catch(error => {
            console.log(error);
          });
    },
  }
});
</script>
