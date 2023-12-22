<template id="health-record-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">
            Health Records
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-sm" @click="hideForm =!hideForm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-plus" aria-hidden="true"></i> Add
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
          <!-- Add other form fields as needed -->
        </form>
        <button rel="tooltip" title="Add Health Record" class="btn btn-info btn-sm" @click="addHealthRecord" style="background-color: #08a29e; border-color: #08a29e;">
          <i class="fa fa-plus" aria-hidden="true"></i> Add Health Record
        </button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(healthRecord, index) in healthRecords" :key="index">
        <div class="mr-auto p-2">
          <span><a :href="`/healthRecords/${healthRecord.id}`" style="color: #08a29e;">
            {{ healthRecord.firstName }} {{ healthRecord.lastName }} (
            Sex: {{ healthRecord.sex }}, DOB: {{ new Date(healthRecord.dob).toLocaleDateString() }},
            Weight: {{ healthRecord.weight }}, Height: {{ healthRecord.height }}, click to see more)
          </a></span>
        </div>
        <div class="p-2">
          <a :href="`/healthRecords/${healthRecord.id}`" class="btn btn-info btn-sm" style="background-color: #08a29e; border-color: #08a29e;">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm ml-2" @click="deleteHealthRecord(healthRecord, index)">
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
