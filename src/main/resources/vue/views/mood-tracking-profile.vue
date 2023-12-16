<template id="mood-tracking-profile">
  <app-layout>
    <div v-if="noMoodEntry">
      <p>We're sorry, we were not able to retrieve this mood entry.</p>
      <p>View <a :href="'/moodTracking'">all mood entries</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noMoodEntry">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Mood Entry Profile</div>
          <div class="col" align="right">
            <button
                rel="tooltip"
                title="Update"
                class="btn btn-info btn-simple btn-link"
                @click="updateMoodEntry()"
            >
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button
                rel="tooltip"
                title="Delete"
                class="btn btn-info btn-simple btn-link"
                @click="deleteMoodEntry()"
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
            <input class="form-control" v-model="moodEntry.id" name="id" type="number" readonly />
          </div>
          <div class="form-group">
            <label class="col-form-label">Mood:</label>
            <input class="form-control" v-model="moodEntry.mood" name="mood" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Rating:</label>
            <input class="form-control" v-model="moodEntry.rating" name="rating" type="number" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Notes:</label>
            <input class="form-control" v-model="moodEntry.notes" name="notes" type="text" />
          </div>
          <div class="form-group">
            <label class="col-form-label">Date:</label>
            <input class="form-control" v-model="moodEntry.date" name="date" type="date" />
          </div>
          <div class="form-group">
            <label class="col-form-label">User ID:</label>
            <input class="form-control" v-model="moodEntry.userId" name="userId" type="number" />
          </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("mood-tracking-profile", {
  template: "#mood-tracking-profile",
  data: () => ({
    moodEntry: null,
    noMoodEntry: false
  }),
  created: function () {
    const entryId = this.$javalin.pathParams["mood-tracking-id"];
    const url = `/api/moodTracking/${entryId}`;
    axios.get(url)
        .then(res => this.moodEntry = res.data)
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
        date: this.moodEntry.date,
        userId: this.moodEntry.userId
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
