<template id="health-record-profile">
  <app-layout>
    <div v-if="noHealthRecord">
      <p>We're sorry, we were not able to retrieve this health record.</p>
      <p>View <a :href="'/healthRecords'">all health records</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noHealthRecord">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;"> Health Record Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateHealthRecord()"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteHealthRecord()"
            >
              <i class="fas fa-trash" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-record-id">Record ID</span>
            </div>
            <input class="form-control" v-model="healthRecord.id" name="id" type="number" readonly placeholder="Record ID"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-timestamp">Timestamp</span>
            </div>
            <input class="form-control" v-model="healthRecord.timestamp" name="timestamp" type="text" readonly placeholder="Timestamp"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-first-name">First Name</span>
            </div>
            <input class="form-control" v-model="healthRecord.firstName" name="firstName" type="text" placeholder="First Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-last-name">Last Name</span>
            </div>
            <input class="form-control" v-model="healthRecord.lastName" name="lastName" type="text" placeholder="Last Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-sex">Sex</span>
            </div>
            <input class="form-control" v-model="healthRecord.sex" name="sex" type="text" placeholder="Sex"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-dob">Date of Birth</span>
            </div>
            <input class="form-control" v-model="dob" name="dob" type="date" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-weight">Weight</span>
            </div>
            <input class="form-control" v-model="healthRecord.weight" name="weight" type="number" placeholder="Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-height">Height</span>
            </div>
            <input class="form-control" v-model="healthRecord.height" name="height" type="number" placeholder="Height"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-blood-type">Blood Type</span>
            </div>
            <input class="form-control" v-model="healthRecord.bloodType" name="bloodType" type="text" placeholder="Blood Type"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-allergies">Allergies</span>
            </div>
            <input class="form-control" v-model="healthRecord.allergies" name="allergies" type="text" placeholder="Allergies"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-medical-conditions">Medical Conditions</span>
            </div>
            <input class="form-control" v-model="healthRecord.medicalConditions" name="medicalConditions" type="text" placeholder="Medical Conditions"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-medications">Medications</span>
            </div>
            <input class="form-control" v-model="healthRecord.medications" name="medications" type="text" placeholder="Medications"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-notes">Notes</span>
            </div>
            <input class="form-control" v-model="healthRecord.notes" name="notes" type="text" placeholder="Notes"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-id">User ID</span>
            </div>
            <input class="form-control" v-model="healthRecord.userId" name="userId" type="number" placeholder="User ID"/>
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 200px;
}
</style>

<script>
app.component("health-record-profile", {
  template: "#health-record-profile",
  data: () => ({
    healthRecord: null,
    noHealthRecord: false
  }),
  created: function () {
    const hrId = this.$javalin.pathParams["health-record-id"];
    const url = `/api/healthRecords/${hrId}`;
    axios.get(url)
        .then(res => {
          this.healthRecord = res.data;

          // Format the date of birth from the health record
          // Reference: https://stackoverflow.com/questions/47066555/remove-time-after-converting-date-toisostring
          const dobDate = new Date(this.healthRecord.dob);
          const formattedDob = dobDate.toISOString().split('T')[0];

          this.dob = formattedDob; // Set the formatted date of birth
        })
        .catch(() => {
          console.error("Error while fetching health record: " + hrId);
          this.noHealthRecord = true;
        });
  },
  methods: {
    updateHealthRecord: function () {
      const hrId = this.$javalin.pathParams["health-record-id"];
      const url = `/api/healthRecords/${hrId}`;
      // Modify the data structure to match your health record properties
      const updatedHealthRecord = {
        id: this.healthRecord.id,
        timestamp: this.healthRecord.timestamp,
        firstName: this.healthRecord.firstName,
        lastName: this.healthRecord.lastName,
        sex: this.healthRecord.sex,
        dob: this.dob,
        weight: this.healthRecord.weight,
        height: this.healthRecord.height,
        bloodType: this.healthRecord.bloodType,
        allergies: this.healthRecord.allergies,
        medicalConditions: this.healthRecord.medicalConditions,
        medications: this.healthRecord.medications,
        notes: this.healthRecord.notes,
        userId: this.healthRecord.userId
      };

      axios.patch(url, updatedHealthRecord)
          .then(response => {
            const responseData = response.data;
            Object.assign(this.healthRecord, responseData);
            alert("Health Record updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating Health Record");
          });
    },
    deleteHealthRecord: function () {
      if (confirm("Do you really want to delete this health record?")) {
        const hrId = this.$javalin.pathParams["health-record-id"];
        const url = `/api/healthRecords/${hrId}`;

        axios.delete(url)
            .then(() => {
              alert("Health Record deleted");
              // Redirect to the health records endpoint
              window.location.href = '/healthRecords';
            })
            .catch(error => {
              console.log(error);
              alert("Error deleting Health Record");
            });
      }
    }
  }
});
</script>
