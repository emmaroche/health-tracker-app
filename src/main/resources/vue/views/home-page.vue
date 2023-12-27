<template id="home-page">
  <app-layout>
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 mt-5">
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Registered Users</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Total Activities</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Total Fitness Goals</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{fitnessGoals.length}} fitness goals</h5>
            <a href="/fitnessGoals" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Total Weight Goals</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{weightGoals.length}} weight goals</h5>
            <a href="/weightGoals" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Total Nutrition Goals</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{nutritionGoals.length}} nutrition goals</h5>
            <a href="/nutritionGoals" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Health Records</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{healthRecords.length}} health records</h5>
            <a href="/healthRecords" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Sleep Tracking</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{sleepTracking.length}} sleep entries</h5>
            <a href="/sleepTracking" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col mb-4">
        <div class="card" style="width: 300px;">
          <h5 class="card-header text-center">Mood Tracking</h5>
          <div class="card-body text-center" style="height: 130px;">
            <h5 class="card-title mb-3">{{moodTracking.length}} mood entries</h5>
            <a href="/moodTracking" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
        <div class="col mb-4"></div>
    </div>
  </app-layout>
</template>

<style>
/* Custom styles for buttons */
.card-body .btn-primary {
  background-color: #08a29e;
  border-color: #08a29e;
  color: #fff;
}
</style>

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
        healthRecords: [],
        sleepTracking: [],
        moodTracking: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("There are currently no users to display"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("There are currently no activities to display"));
        axios.get("/api/fitnessGoals")
            .then(res => this.fitnessGoals = res.data)
            .catch(() => alert("There are currently no fitness goals to display"));
        axios.get("/api/weightGoals")
            .then(res => this.weightGoals = res.data)
            .catch(() => alert("There are currently no weight goals to display"));
        axios.get("/api/nutritionGoals")
            .then(res => this.nutritionGoals = res.data)
            .catch(() => alert("There are currently no nutrition goals to display"));
        axios.get("/api/healthRecords")
            .then(res => this.healthRecords = res.data)
            .catch(() => alert("There are currently no health records to display"));
        axios.get("/api/sleepTracking")
            .then(res => this.sleepTracking = res.data)
            .catch(() => alert("There are currently no sleep tracking entries to display"));
        axios.get("/api/moodTracking")
            .then(res => this.moodTracking = res.data)
            .catch(() => alert("There are currently no mood tracking entries to display"));
      }
    });
</script>
