<template id="mood-tracking-profile">
  <app-layout>
    <div v-if="noMoodEntry">
      <p>We're sorry, we were not able to retrieve this mood entry.</p>
      <p>View <a :href="'/moodTracking'">all mood entries</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noMoodEntry">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;"> Mood Entry Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateMoodEntry"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteMoodEntry"
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
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-description">Mood</span>
            </div>
            <input class="form-control" v-model="moodEntry.mood" name="mood" type="text" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Rating / 10</span>
            </div>
            <input class="form-control" v-model="moodEntry.rating" name="rating" type="number" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-calories">Notes</span>
            </div>
            <input class="form-control" v-model="moodEntry.notes" name="notes" type="text" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-started">Date</span>
            </div>
            <input class="form-control" v-model="date" name="date" type="date" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-userId">User ID</span>
            </div>
            <input class="form-control" v-model="moodEntry.userId" name="userId" type="number" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-sleepId">Sleep ID</span>
            </div>
            <input class="form-control" v-model="moodEntry.sleepId" name="sleepId" type="number" />
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 150px;
}
</style>

<script>
app.component("mood-tracking-profile", {
  template: "#mood-tracking-profile",
  data: () => ({
    moodEntry: null,
    noMoodEntry: false,
    date: null,
  }),
  created: function () {
    const entryId = this.$javalin.pathParams["mood-tracking-id"];
    const url = `/api/moodTracking/${entryId}`;
    axios.get(url)
        .then(res => {
          this.moodEntry = res.data;

          // Format the date from the mood entry
          // Reference: https://stackoverflow.com/questions/47066555/remove-time-after-converting-date-toisostring
          const moodDate = new Date(this.moodEntry.date);
          const formattedDate = moodDate.toISOString().split('T')[0];

          this.date = formattedDate; // Set the formatted date
        })
        .catch(() => {
          console.error("Error while fetching mood entry: " + entryId);
          this.noMoodEntry = true;
        });
  },
  methods: {
    updateMoodEntry: function () {
      const entryId = this.$javalin.pathParams["mood-tracking-id"];
      const url = `/api/moodTracking/${entryId}`;

      const updatedMoodEntry = {
        id: this.moodEntry.id,
        mood: this.moodEntry.mood,
        rating: this.moodEntry.rating,
        notes: this.moodEntry.notes,
        date: this.date,
        userId: this.moodEntry.userId,
        sleepId: this.moodEntry.sleepId
      };

      axios.patch(url, updatedMoodEntry)
          .then(response => {
            // Extract the data from the response
            const responseData = response.data;

            // Update the moodEntry with the extracted data
            Object.assign(this.moodEntry, responseData);

            alert("Mood Entry updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating Mood Entry");
          });
    },
    deleteMoodEntry: function () {
      if (confirm("Do you really want to delete this mood entry?")) {
        const entryId = this.$javalin.pathParams["mood-tracking-id"];
        const url = `/api/moodTracking/${entryId}`;
        axios.delete(url)
            .then(() => {
              alert("Mood Entry deleted");
              // Redirect to the mood entries endpoint
              window.location.href = '/moodTracking';
            })
            .catch(error => {
              console.log(error);
              alert("Error deleting Mood Entry");
            });
      }
    }
  }
});
</script>
