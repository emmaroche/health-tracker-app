<template id="health-record-overview">
  <app-layout>
    <div class="card bg-light mt-4 mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">Health Records</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Add"
                class="btn btn-info btn-sm"
                @click="hideForm = !hideForm"
                style="background-color: #08a29e; border-color: #08a29e;"
            >
              <i class="fa fa-plus" aria-hidden="true"></i> Add
            </button>
          </div>
        </div>
      </div>
      <div class="col-12 ml-2 mb-3 mt-3" style="font-weight: 400;"> Efficiently manage your health records, including details such as name, sex, date of birth, and more with our Health Records feature. </div>
      <div class="card-body" :class="{ 'd-none': hideForm }">
        <form id="addHealthRecord">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-firstName">First Name</span>
            </div>
            <input
                type="text"
                class="form-control"
                v-model="formData.firstName"
                name="firstName"
                placeholder="First Name"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-lastName">Last Name</span>
            </div>
            <input
                type="text"
                class="form-control"
                v-model="formData.lastName"
                name="lastName"
                placeholder="Last Name"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-sex">Sex</span>
            </div>
            <input type="text" class="form-control" v-model="formData.sex" name="sex" placeholder="Sex" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-dob">Date of Birth</span>
            </div>
            <input type="text" class="form-control" v-model="formData.dob" name="dob" placeholder="Date of Birth" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-weight">Weight</span>
            </div>
            <input
                type="number"
                class="form-control"
                v-model="formData.weight"
                name="weight"
                placeholder="Weight"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-height">Height</span>
            </div>
            <input
                type="number"
                class="form-control"
                v-model="formData.height"
                name="height"
                placeholder="Height"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-bloodType">Blood Type</span>
            </div>
            <input
                type="text"
                class="form-control"
                v-model="formData.bloodType"
                name="bloodType"
                placeholder="Blood Type"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-allergies">Allergies</span>
            </div>
            <input
                type="text"
                class="form-control"
                v-model="formData.allergies"
                name="allergies"
                placeholder="Allergies"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-medicalConditions">Medical Conditions</span>
            </div>
            <input
                type="text"
                class="form-control"
                v-model="formData.medicalConditions"
                name="medicalConditions"
                placeholder="Medical Conditions"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-medications">Medications</span>
            </div>
            <input
                type="text"
                class="form-control"
                v-model="formData.medications"
                name="medications"
                placeholder="Medications"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-notes">Notes</span>
            </div>
            <input
                type="text"
                class="form-control"
                v-model="formData.notes"
                name="notes"
                placeholder="Notes"
            />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-health-record-userId">User ID</span>
            </div>
            <input
                type="number"
                class="form-control"
                v-model="formData.userId"
                name="userId"
                placeholder="User ID"
            />
          </div>
        </form>
        <button
            rel="tooltip"
            title="Add Health Record"
            class="btn btn-info btn-sm mt-3"
            @click="addHealthRecord"
            style="background-color: #08a29e; border-color: #08a29e;"
        >
          <i class="fa fa-plus" aria-hidden="true"></i> Add Health Record
        </button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="col-6 mb-3" style="font-weight: 600;">Current Health Record</div>
      <div
          class="list-group-item d-flex align-items-start"
          v-for="(healthRecord, index) in healthRecords"
          :key="index"
      >
        <div class="mr-auto p-2">
          <span>
            <a :href="`/healthRecords/${healthRecord.id}`" style="color: #08a29e;">
              {{ healthRecord.firstName }} {{ healthRecord.lastName }} (
              Sex: {{ healthRecord.sex }}, DOB: {{ new Date(healthRecord.dob).toLocaleDateString() }},
              Weight: {{ healthRecord.weight }}, Height: {{ healthRecord.height }}, click to see more)
            </a>
          </span>
        </div>
        <div class="p-2">
          <div class="btn-group d-flex" role="group">
            <a :href="`/healthRecords/${healthRecord.id}`" class="btn btn-info btn-sm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </a>
            <div class="mr-2"></div>
            <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm" @click="deleteHealthRecord(healthRecord, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
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
app.component("health-record-overview", {
  template: "#health-record-overview",
  data: () => ({
    healthRecords: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchHealthRecords();
  },
  methods: {
    fetchHealthRecords: function () {
      axios.get("/api/healthRecords")
          .then(res => this.healthRecords = res.data)
          .catch(() => alert("We couldn't find any health records at the moment. Feel free to add a new health record, wait a moment, or refresh the page to check again"));
    },
    deleteHealthRecord: function (healthRecord, index) {
      if (confirm('Are you sure you want to delete this health record? This action cannot be undone.', 'Warning')) {
        const recordId = healthRecord.id;
        const url = `/api/healthRecords/${recordId}`;
        axios.delete(url)
            .then(response =>
                this.healthRecords.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error);
            });
      }
    },
    addHealthRecord: function () {
      const url = "/api/healthRecords";
      axios.post(url, this.formData)
          .then(response => {
            this.healthRecords.push(response.data);
            // Reset form data after adding health record
            this.formData = {};
          })
          .catch(error => {
            console.log(error);
          });
    }
  }
});
</script>
