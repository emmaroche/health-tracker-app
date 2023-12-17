<template id="mood-tracking-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">Mood Tracking</div>
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
        <form id="addMoodTracking">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-mood-quality">Mood</span>
            </div>
            <input type="text" class="form-control" v-model="formData.mood" name="mood" placeholder="Mood"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-mood-rating">Rating</span>
            </div>
            <input type="number" class="form-control" v-model="formData.rating" name="rating" placeholder="Rating"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-mood-notes">Notes</span>
            </div>
            <input type="text" class="form-control" v-model="formData.notes" name="notes" placeholder="Notes"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-mood-date">Date</span>
            </div>
            <input type="date" class="form-control" v-model="formData.date" name="date"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-mood-userId">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addMoodEntry()">Add Mood Entry</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(moodEntry, index) in moodTracking" :key="index">
        <div class="mr-auto p-2">
          <span>
            <a :href="`/moodTracking/${moodEntry.id}`">
              {{ moodEntry.mood }} (Rating: {{ moodEntry.rating }}, Notes: {{ moodEntry.notes }}, Date: {{ moodEntry.date }}, User ID: {{ moodEntry.userId }})
            </a>
          </span>
        </div>
        <div class="p-2">
          <a :href="`/moodTracking/${moodEntry.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteMoodEntry(moodEntry, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("mood-tracking-overview", {
  template: "#mood-tracking-overview",
  data: () => ({
    moodTracking: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchMoodEntries();
  },
  methods: {
    fetchMoodEntries: function () {
      axios.get("/api/moodTracking")
          .then(res => this.moodTracking = res.data)
          .catch(() => alert("Error while fetching mood entries"));
    },
    deleteMoodEntry: function (moodEntry, index) {
      if (confirm('Are you sure you want to delete this mood entry? This action cannot be undone.', 'Warning')) {
        const moodEntryId = moodEntry.id;
        const url = `/api/moodTracking/${moodEntryId}`;
        axios.delete(url)
            .then(response =>
                this.moodTracking.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addMoodEntry: function () {
      const url = "/api/moodTracking";
      axios.post(url, this.formData)
          .then(response => {
            this.moodTracking.push(response.data);
            this.hideForm = true;
            // Reset form data after adding mood entry
            this.formData = {
              mood: '',
              rating: null,
              notes: '',
              date: null,
              userId: null,
            };
          })
          .catch(error => {
            console.log(error);
          });
    },
  }
});
</script>
