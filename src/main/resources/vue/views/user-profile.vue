<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p>We're sorry, we were not able to retrieve this user.</p>
      <p>View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">User Profile</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link mr-2"
                    @click="updateUser()">
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-name">Username</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-phone">Phone Number</span>
            </div>
            <input type="number" class="form-control" v-model="user.phoneNumber" name="phoneNumber" placeholder="Phone Number"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-address">Address</span>
            </div>
            <input type="text" class="form-control" v-model="user.address" name="address" placeholder="Address"/>
          </div>
        </form>
      </div>

      <div class="card-footer text-left">
        <p v-if="activities.length === 0 && healthRecords.length === 0 && fitnessGoals.length === 0 && weightGoals.length === 0">
          No activities, health records, or goals yet...
        </p>
        <p v-else>
         View the following health related data for this user:
        </p>
        <div class="card-footer text-center">
          <div v-if="user">
            <div class="btn-group-vertical" role="group" aria-label="User Actions">
              <a :href="`/users/${user.id}/activities`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-running"></i> View User Activities
              </a>
              <a :href="`/users/${user.id}/healthRecords`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-heartbeat"></i> View Health Record
              </a>
              <a :href="`/users/${user.id}/fitnessGoals`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-dumbbell"></i> View Fitness Goals
              </a>
              <a :href="`/users/${user.id}/weightGoals`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-weight"></i> View Weight Goals
              </a>
              <a :href="`/users/${user.id}/nutritionGoals`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-apple-alt"></i> View Nutrition Goals
              </a>
              <a :href="`/users/${user.id}/sleepTracking`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-moon"></i> View Sleep Entries
              </a>
              <a :href="`/users/${user.id}/moodTracking`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-smile"></i> View Mood Entries
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
app.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    healthRecords: [],
    fitnessGoals: [],
    weightGoals: [],
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        });
    axios.get(url + `/healthRecords`)
        .then(res => this.healthRecords = res.data)
        .catch(error => {
          console.log("No health records added yet (this is ok): " + error)
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
    axios.get(url + `/fitnessGoals`)
        .then(res => this.fitnessGoals = res.data)
        .catch(error => {
          console.log("No fitness goals added yet (this is ok): " + error)
        })
    axios.get(url + `/weightGoals`)
        .then(res => this.weightGoals = res.data)
        .catch(error => {
          console.log("No weight goals added yet (this is ok): " + error)
        })
  },
  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`
      axios.patch(url,
          {
            name: this.user.name,
            email: this.user.email,
            phoneNumber: this.user.phoneNumber,
            address: this.user.address
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User updated!")
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`
        axios.delete(url)
            .then(response => {
              alert("User deleted")
              //display the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>
