<template id="user-health-record-overview">
  <app-layout>
    <div>
      <h3>Health Record List</h3>
      <ul>
        <li v-for="healthRecord in healthRecords">
          {{ healthRecord.id }}: {{ healthRecord.firstName }} {{ healthRecord.lastName }} (Timestamp: {{ healthRecord.timestamp }}, Sex: {{ healthRecord.sex }}, DOB: {{ healthRecord.dob }}, Weight: {{ healthRecord.weight }}, Height: {{ healthRecord.height }}, Blood Type: {{ healthRecord.bloodType }}, Allergies: {{ healthRecord.allergies }}, Medical Conditions: {{ healthRecord.medicalConditions }}, Medications: {{ healthRecord.medications }}, Notes: {{ healthRecord.notes }}, User ID: {{ healthRecord.userId }})
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-health-record-overview",{
  template: "#user-health-record-overview",
  data: () => ({
    healthRecords: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/healthRecords`)
        .then(res => this.healthRecords = res.data)
        .catch(() => alert("We couldn't find any health records at the moment. Feel free to add a new health record, wait a moment, or refresh the page to check again"));
  }
});
</script>