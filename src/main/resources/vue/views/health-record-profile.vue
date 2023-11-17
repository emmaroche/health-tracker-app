<template id="health-record-profile">
  <app-layout>
    <div v-if="!healthRecord">
      <p>We're sorry, we were not able to retrieve this health record.</p>
      <p>View <a :href="'/health-records'">all health records</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="healthRecord">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Health Record Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link"
                @click="updateHealthRecord()"
            >
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteHealthRecord()"
            >
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="form-group">
            <label class="col-form-label">Record ID:</label>
            <input class="form-control" v-model="healthRecord.id" name="id" type="number" readonly />
          </div>
          <div class="form-group">
            <label class="col-form-label">Timestamp:</label>
            <input class="form-control" v-model="healthRecord.timestamp" name="timestamp" type="text" readonly />
          </div>
          <div class="form-group">
            <label class="col-form-label">First Name:</label>
            <input class="form-control" v-model="healthRecord.firstName" name="firstName" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Last Name:</label>
            <input class="form-control" v-model="healthRecord.lastName" name="lastName" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Sex:</label>
            <input class="form-control" v-model="healthRecord.sex" name="sex" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Date of Birth:</label>
            <input class="form-control" v-model="healthRecord.dob" name="dob" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Weight:</label>
            <input class="form-control" v-model="healthRecord.weight" name="weight" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Height:</label>
            <input class="form-control" v-model="healthRecord.height" name="height" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Blood Type:</label>
            <input class="form-control" v-model="healthRecord.bloodType" name="bloodType" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Allergies:</label>
            <input class="form-control" v-model="healthRecord.allergies" name="allergies" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Medical Conditions:</label>
            <input class="form-control" v-model="healthRecord.medicalConditions" name="medicalConditions" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Medications:</label>
            <input class="form-control" v-model="healthRecord.medications" name="medications" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Notes:</label>
            <input class="form-control" v-model="healthRecord.notes" name="notes" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">User ID:</label>
            <input class="form-control" v-model="healthRecord.userId" name="userId" type="number" />
          </div>
        </form>
      </div>
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
    axios.get(url)
        .then(res => this.healthRecord = res.data)
        .catch(() => {
          console.error("Error while fetching health record: " + hrId);
          this.healthRecord = null;
        });
  },
  methods: {
    updateHealthRecord: function () {
      // Add your logic to update the health record here
      alert("Health Record updated!");
    },
    deleteHealthRecord: function () {
      if (confirm("Do you really want to delete this health record?")) {
        // Add your logic to delete the health record here
        alert("Health Record deleted");
        // Redirect to the health records endpoint
        window.location.href = '/health-records';
      }
    }
  }
});
</script>
