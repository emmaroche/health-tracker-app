<!-- weight-goals-user-weight-overview-template -->
<template id="weight-goals-user-weight-overview">
  <app-layout>
    <div>
      <h3>User Weight Data</h3>
      <ul>
        <li v-for="currentWeight in userWeight">
          Your weight was {{ currentWeight.currentWeight }} kg on {{ currentWeight.currentWeightTimestamp }}
        </li>
      </ul>

      <!-- Display the form when 'editMode' is true -->
      <form v-if="editMode" @submit.prevent="saveUserWeight">
        <label for="currentWeight">Updated Weight (kg):</label>
        <input v-model="editedUserWeight.currentWeight" type="number" step="0.1" required />

        <button type="submit">Save</button>
        <button @click="cancelEdit">Cancel</button>
      </form>

      <!-- Display the button to enter edit mode -->
      <button v-else @click="enterEditMode">Update User Weight</button>
    </div>
  </app-layout>
</template>

<script>
app.component("weight-goals-user-weight-overview", {
  template: "#weight-goals-user-weight-overview",
  data: () => ({
    userWeight: [],
    editMode: false,
    editedUserWeight: {
      currentWeight: 0,  // Set a default value
      currentWeightTimestamp: '',
    },
  }),
  created() {
    const wgID = this.$javalin.pathParams["weight-goals-id"];

    // Fetch User Weight Data
    axios.get(`/api/weightGoals/${wgID}/userWeight`)
        .then(res => {
          this.userWeight = res.data;

          // Set editedUserWeight to the first item in the userWeight array (if it exists)
          if (this.userWeight.length > 0) {
            this.editedUserWeight = { ...this.userWeight[0] };
          }
        })
        .catch(() => alert("Error while fetching user weight data"));
  },

  methods: {
    enterEditMode: function () {
      // Enter edit mode, set initial values
      this.editMode = true;
    },
    cancelEdit: function () {
      // Cancel edit mode
      this.editMode = false;
    },
    saveUserWeight: function () {
      // Check if this.editedUserWeight.currentWeight is null or not a valid number
      if (this.editedUserWeight.currentWeight === null || isNaN(parseFloat(this.editedUserWeight.currentWeight))) {
        alert("Please enter a valid value for updated weight.");
        return;
      }
      const userWeightId = this.editedUserWeight.id;
      const url = `/api/userWeight/${userWeightId}`;

      // Automatically set the timestamp to today's date
      const today = new Date();
      const formattedTimestamp = today.toISOString();

      const updatedUserWeight = {
        id: this.editedUserWeight.id,
        currentWeight: parseFloat(this.editedUserWeight.currentWeight),
        currentWeightTimestamp: formattedTimestamp,
        weightGoalId: this.editedUserWeight.weightGoalId,
        userId: this.editedUserWeight.userId
      };

      console.log("Updating user weight with data:", updatedUserWeight);

      axios.patch(url, updatedUserWeight)
          .then(response => {
            console.log("Update response:", response.data);
            const responseData = response.data;
            Object.assign(this.editedUserWeight, responseData);
            this.editMode = false; // Exit edit mode after a successful update
            alert("User Weight updated!");
          })
          .catch(error => {
            console.error("Update error:", error);
            alert("Error updating User Weight");
          });
    },
  }
});
</script>
