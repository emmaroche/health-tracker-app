<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p>We're sorry, we were not able to retrieve this user.</p>
      <p>View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile</div>
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
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-center">
        <div v-if="user">
          <br>
          <a :href="`/users/${user.id}/activities`">View User Activities</a>
          <br>
          <a :href="`/users/${user.id}/healthRecords`">View Health Record</a>
          <br>
          <a :href="`/users/${user.id}/fitnessGoals`">View Fitness Goals</a>
          <br>
          <a :href="`/users/${user.id}/weightGoals`">View Weight Goals</a>
          <br>
        </div>
      </div>

      <div class="card-footer text-left">
        <p v-if="activities.length === 0 && healthRecords.length === 0 && fitnessGoals.length === 0 && weightGoals.length === 0">
          No activities, health records, or goals yet...
        </p>
        <p v-else>
          Data so far...
        </p>

        <ul v-if="activities.length > 0">
          <li v-for="activity in activities">
            {{ activity.description }} for {{ activity.duration }} minutes
          </li>
        </ul>

        <ul v-if="healthRecords.length > 0">
          <li v-for="record in healthRecords">
            Health Record: {{ record.lastName }} - {{ record.firstName }}
          </li>
        </ul>

        <ul v-if="fitnessGoals.length > 0">
          <li v-for="goal in fitnessGoals">
            Fitness Goal: {{ goal.type }} - {{ goal.workoutsPerWeek }}
          </li>
        </ul>

        <ul v-if="weightGoals.length > 0">
          <li v-for="goal in weightGoals">
            Weight Goal: {{ goal.type }} - {{ goal.startingWeight }}
          </li>
        </ul>
      </div>


    </div>
  </app-layout>
</template>

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
            email: this.user.email
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