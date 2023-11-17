<template id="health-record-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Health Records
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addHealthRecord">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-firstName">First Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.firstName" name="firstName" placeholder="First Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-lastName">Last Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.lastName" name="lastName" placeholder="Last Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-timestamp">Timestamp</span>
            </div>
            <input type="text" class="form-control" v-model="formData.timestamp" name="timestamp" placeholder="Timestamp"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-sex">Sex</span>
            </div>
            <input type="text" class="form-control" v-model="formData.sex" name="sex" placeholder="Sex"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-dob">Date of Birth</span>
            </div>
            <input type="text" class="form-control" v-model="formData.dob" name="dob" placeholder="Date of Birth"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-weight">Weight</span>
            </div>
            <input type="text" class="form-control" v-model="formData.weight" name="weight" placeholder="Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-height">Height</span>
            </div>
            <input type="text" class="form-control" v-model="formData.height" name="height" placeholder="Height"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-bloodType">Blood Type</span>
            </div>
            <input type="text" class="form-control" v-model="formData.bloodType" name="bloodType" placeholder="Blood Type"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-allergies">Allergies</span>
            </div>
            <input type="text" class="form-control" v-model="formData.allergies" name="allergies" placeholder="Allergies"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-medicalConditions">Medical Conditions</span>
            </div>
            <input type="text" class="form-control" v-model="formData.medicalConditions" name="medicalConditions" placeholder="Medical Conditions"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-medications">Medications</span>
            </div>
            <input type="text" class="form-control" v-model="formData.medications" name="medications" placeholder="Medications"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-notes">Notes</span>
            </div>
            <input type="text" class="form-control" v-model="formData.notes" name="notes" placeholder="Notes"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-health-record-userId">User ID</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addHealthRecord()">Add Health Record</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(healthRecord, index) in healthRecords" :key="index">
        <div class="mr-auto p-2">
          <span><a :href="`/healthRecords/${healthRecord.id}`">
            {{ healthRecord.firstName }} {{ healthRecord.lastName }} (Timestamp: {{ healthRecord.timestamp }},
            Sex: {{ healthRecord.sex }}, DOB: {{ healthRecord.dob }},
            Weight: {{ healthRecord.weight }}, Height: {{ healthRecord.height }},
            Blood Type: {{ healthRecord.bloodType }}, Allergies: {{ healthRecord.allergies }},
            Medical Conditions: {{ healthRecord.medicalConditions }}, Medications: {{ healthRecord.medications }},
            Notes: {{ healthRecord.notes }}, User ID: {{ healthRecord.userId }})
          </a></span>
        </div>
        <div class="p-2">
          <a :href="`/healthRecords/${healthRecord.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteHealthRecord(healthRecord, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("health-record-overview", {
  template: "#health-record-overview",
  data: () => ({
    healthRecords: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchHealthRecords();
  },
  methods: {
    fetchHealthRecords: function () {
      axios.get("/api/healthRecords")
          .then(res => this.healthRecords = res.data)
          .catch(() => alert("Error while fetching health records"));
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