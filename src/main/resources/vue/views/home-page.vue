<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Fitness Goals</h5>
          <div class="card-body">
            <h5 class="card-title">{{fitnessGoals.length}} fitness goals</h5>
            <a href="/fitnessGoals" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Weight Goals</h5>
          <div class="card-body">
            <h5 class="card-title">{{weightGoals.length}} weight goals</h5>
            <a href="/weightGoals" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Nutrition Goals</h5>
          <div class="card-body">
            <h5 class="card-title">{{nutritionGoals.length}} nutrition goals</h5>
            <a href="/nutritionGoals" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Health Records</h5>
          <div class="card-body">
            <h5 class="card-title">{{healthRecords.length}} health records</h5>
            <a href="/healthRecords" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>

  </app-layout>
</template>

<script>
app.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        fitnessGoals: [],
        weightGoals: [],
        nutritionGoals: [],
        healthRecords: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/fitnessGoals")
            .then(res => this.fitnessGoals = res.data)
            .catch(() => alert("Error while fetching fitness goals"));
        axios.get("/api/weightGoals")
            .then(res => this.weightGoals = res.data)
            .catch(() => alert("Error while fetching weight goals"));
        axios.get("/api/nutritionGoals")
            .then(res => this.nutritionGoals = res.data)
            .catch(() => alert("Error while fetching nutrition goals"));
        axios.get("/api/healthRecords")
            .then(res => this.healthRecords = res.data)
            .catch(() => alert("Error while fetching health records"));
      }
    });
</script>