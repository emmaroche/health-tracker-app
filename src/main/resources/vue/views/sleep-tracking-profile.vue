<template id="sleep-tracking-profile">
  <app-layout>
    <div v-if="noSleepEntry">
      <p>We're sorry, we were not able to retrieve this sleep entry.</p>
      <p>View <a :href="'/sleepTracking'">all sleep entries</a>.</p>
    </div>
    <div class="card bg-light mt-4 mb-3" v-if="!noSleepEntry">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;"> Sleep Entry Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link mr-2"
                @click="updateSleepEntry"
            >
              <i class="fas fa-edit" aria-hidden="true" style="color: #08a29e;"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteSleepEntry"
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
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-description">Quality</span>
            </div>
            <input class="form-control" v-model="sleepEntry.quality" name="quality" type="text" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-duration">Duration in hours</span>
            </div>
            <input class="form-control" v-model="sleepEntry.duration" name="duration" type="number" />
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-activity-calories">Notes</span>
            </div>
            <input class="form-control" v-model="sleepEntry.notes" name="notes" type="text" />
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
            <input class="form-control" v-model="sleepEntry.userId" name="userId" type="number" />
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <p v-if="moodTracking.length === 0">
          No mood entries associated with this sleep
        </p>
        <p v-else>
          View associated mood entries you have linked to this sleep entry:
        </p>
        <div class="card-footer text-center">
          <div v-if="sleepEntry">
            <div class="btn-group-vertical" role="group" aria-label="Fitness Actions">
              <a :href="`/sleepTracking/${sleepEntry.id}/moodTracking`" class="btn btn-link" style="color: #08a29e;">
                <i class="fas fa-smile"></i> View Mood Entries
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<style>
.custom-label {
  width: 180px;
}
</style>

<script>
app.component("sleep-tracking-profile", {
  template: "#sleep-tracking-profile",
  data: () => ({
    sleepEntry: null,
    //https://stackoverflow.com/questions/47066555/remove-time-after-converting-date-toisostring
    date: null,
    noSleepEntry: false,
    moodTracking: []
  }),
  created: function () {
    const entryId = this.$javalin.pathParams["sleep-tracking-id"];
    const url = `/api/sleepTracking/${entryId}`;
    axios.get(url)
        .then(res => {
          this.sleepEntry = res.data;

          // Format the date from the sleep entry
          // Reference: https://stackoverflow.com/questions/47066555/remove-time-after-converting-date-toisostring
          const sleepDate = new Date(this.sleepEntry.date);
          const formattedDate = sleepDate.toISOString().split('T')[0];

          this.date = formattedDate; // Set the formatted date
        })
        .catch(() => {
          console.error("Error while fetching sleep entry: " + entryId);
          this.noSleepEntry = true;
        });
    axios.get(url + `/moodTracking`)
        .then(res => this.moodTracking = res.data)
        .catch(error => {
          console.log("No mood entries added yet: " + error)
        })
  },
  methods: {
    updateSleepEntry: function () {
      const entryId = this.$javalin.pathParams["sleep-tracking-id"];
      const url = `/api/sleepTracking/${entryId}`;

      const updatedSleepEntry = {
        id: this.sleepEntry.id,
        quality: this.sleepEntry.quality,
        duration: this.sleepEntry.duration,
        notes: this.sleepEntry.notes,
        date: this.date,
        userId: this.sleepEntry.userId
      };

      axios.patch(url, updatedSleepEntry)
          .then(response => {
            // Extract the data from the response
            const responseData = response.data;

            // Update the sleepEntry with the extracted data
            Object.assign(this.sleepEntry, responseData);

            alert("Sleep Entry updated!");
          })
          .catch(error => {
            console.log(error);
            alert("Error updating Sleep Entry");
          });
    },
    deleteSleepEntry: function () {
      if (confirm("Do you really want to delete this sleep entry?")) {
        const entryId = this.$javalin.pathParams["sleep-tracking-id"];
        const url = `/api/sleepTracking/${entryId}`;
        axios.delete(url)
            .then(() => {
              alert("Sleep Entry deleted");
              // Redirect to the sleep entries endpoint
              window.location.href = '/sleepTracking';
            })
            .catch(error => {
              console.log(error);
              alert("Error deleting Sleep Entry");
            });
      }
    }
  }
});
</script>
