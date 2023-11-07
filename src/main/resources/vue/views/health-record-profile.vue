<template id="health-record-profile">
  <app-layout>
    <div>
      <form v-if="healthRecord">
        <label class="col-form-label">Record ID:</label>
        <input class="form-control" v-model="healthRecord.id" name="id" type="number" readonly /><br>
        <label class="col-form-label">Timestamp:</label>
        <input class="form-control" v-model="healthRecord.timestamp" name="timestamp" type="text" readonly /><br>
        <label class="col-form-label">First Name:</label>
        <input class="form-control" v-model="healthRecord.firstName" name="firstName" type="text" /><br>
        <label class="col-form-label">Last Name:</label>
        <input class="form-control" v-model="healthRecord.lastName" name="lastName" type="text" /><br>
        <label class="col-form-label">Sex:</label>
        <input class="form-control" v-model="healthRecord.sex" name="sex" type="text" /><br>
        <label class="col-form-label">Date of Birth:</label>
        <input class="form-control" v-model="healthRecord.dob" name="dob" type="text" /><br>
        <label class="col-form-label">Weight:</label>
        <input class="form-control" v-model="healthRecord.weight" name="weight" type="number" /><br>
        <label class="col-form-label">Height:</label>
        <input class="form-control" v-model="healthRecord.height" name="height" type="number" /><br>
        <label class="col-form-label">Blood Type:</label>
        <input class="form-control" v-model="healthRecord.bloodType" name="bloodType" type="text" /><br>
        <label class="col-form-label">Allergies:</label>
        <input class="form-control" v-model="healthRecord.allergies" name="allergies" type="text" /><br>
        <label class="col-form-label">Medical Conditions:</label>
        <input class="form-control" v-model="healthRecord.medicalConditions" name="medicalConditions" type="text" /><br>
        <label class="col-form-label">Medications:</label>
        <input class="form-control" v-model="healthRecord.medications" name="medications" type="text" /><br>
        <label class="col-form-label">Notes:</label>
        <input class="form-control" v-model="healthRecord.notes" name="notes" type="text" /><br>
        <label class="col-form-label">User ID:</label>
        <input class="form-control" v-model="healthRecord.userId" name="userId" type="number" /><br>
      </form>
    </div>
  </app-layout>
</template>

<script>
app.component("health-record-profile", {
  template: "#health-record-profile",
  data: () => ({
    healthRecord: null
  }),
  created: function () {
    const hrId = this.$javalin.pathParams["health-record-id"];
    const url = `/api/healthRecords/${hrId}`;
    console.log("Fetching health record data from URL: " + url);
    axios.get(url)
        .then(res => {
          console.log("Received data:", res.data);
          this.healthRecord = res.data;
        })
        .catch(error => {
          console.error("Error while fetching health record: " + hrId, error);
          alert("Error while fetching health record" + hrId);
        });
  }

});
</script>
