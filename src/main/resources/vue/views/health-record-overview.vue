<template id="health-record-overview">
  <app-layout>
    <div>
      <div>
        <ul class="health-record-overview-list">
          <li v-for="healthRecord in healthRecords">
            <a :href="`/healthRecords/${healthRecord.id}`">{{ healthRecord.firstName }} {{ healthRecord.lastName }} (Timestamp: {{ healthRecord.timestamp }}, Sex: {{ healthRecord.sex }}, DOB: {{ healthRecord.dob }}, Weight: {{ healthRecord.weight }}, Height: {{ healthRecord.height }}, Blood Type: {{ healthRecord.bloodType }}, Allergies: {{ healthRecord.allergies }}, Medical Conditions: {{ healthRecord.medicalConditions }}, Medications: {{ healthRecord.medications }}, Notes: {{ healthRecord.notes }}, User ID: {{ healthRecord.userId }})</a>
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("health-record-overview", {
  template: "#health-record-overview",
  data: () => ({
    healthRecords: [],
  }),
  created() {
    this.fetchHealthRecords();
  },
  methods: {
    fetchHealthRecords: function () {
      axios.get("/api/healthRecords")
          .then(res => this.healthRecords = res.data)
          .catch(() => alert("Error while fetching health records"));
    }
  }
});
</script>