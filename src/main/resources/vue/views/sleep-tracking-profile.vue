<template id="sleep-tracking-profile">
  <app-layout>
    <div v-if="noSleepEntry">
      <p>We're sorry, we were not able to retrieve this sleep entry.</p>
      <p>View <a :href="'/sleepTracking'">all sleep entries</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noSleepEntry">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Sleep Entry Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link"
                @click="updateSleepEntry()"
            >
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteSleepEntry()"
            >
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="form-group">
            <label class="col-form-label">Entry ID:</label>
            <input class="form-control" v-model="sleepEntry.id" name="id" type="number" readonly />
          </div>
          <div class="form-group">
            <label class="col-form-label">Quality:</label>
            <input class="form-control" v-model="sleepEntry.quality" name="quality" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Duration (minutes):</label>
            <input class="form-control" v-model="sleepEntry.duration" name="duration" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Notes:</label>
            <input class="form-control" v-model="sleepEntry.notes" name="notes" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Date:</label>
            <input class="form-control" v-model="sleepEntry.date" name="date" type="date" />
          </div>
          <div class="form-group">
            <label class="col-form-label">User ID:</label>
            <input class="form-control" v-model="sleepEntry.userId" name="userId" type="number" />
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("sleep-tracking-profile", {
  template: "#sleep-tracking-profile",
  data: () => ({
    sleepEntry: null,
    noSleepEntry: false
  }),
  created: function () {
    const entryId = this.$javalin.pathParams["sleep-tracking-id"];
    const url = `/api/sleepTracking/${entryId}`;
    axios.get(url)
        .then(res => this.sleepEntry = res.data)
        .catch(() => {
          console.error("Error while fetching sleep entry: " + entryId);
          this.noSleepEntry = true;
        });
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
        date: this.sleepEntry.date,
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