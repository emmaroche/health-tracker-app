<template id="sleep-tracking-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">Sleep Tracking</div>
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
        <form id="addSleepTracking">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-type">Quality</span>
            </div>
            <input type="text" class="form-control" v-model="formData.quality" name="quality" placeholder="Quality"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-duration">Duration</span>
            </div>
            <input type="number" class="form-control" v-model="formData.duration" name="duration" placeholder="Duration"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-notes">Notes</span>
            </div>
            <input type="text" class="form-control" v-model="formData.notes" name="notes" placeholder="Notes"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-date">Date</span>
            </div>
            <input type="date" class="form-control" v-model="formData.date" name="date"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-userId">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User ID"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addSleepEntry()">Add Sleep Entry</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(sleepEntry, index) in sleepTracking" :key="index">
        <div class="mr-auto p-2">
          <span>
            <a :href="`/sleepTracking/${sleepEntry.id}`">
              {{ sleepEntry.quality }} (Duration: {{ sleepEntry.duration }} minutes, Notes: {{ sleepEntry.notes }}, Date: {{ sleepEntry.date }}, User ID: {{ sleepEntry.userId }})
            </a>
          </span>
        </div>
        <div class="p-2">
          <a :href="`/sleepTracking/${sleepEntry.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link" @click="deleteSleepEntry(sleepEntry, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("sleep-tracking-overview", {
  template: "#sleep-tracking-overview",
  data: () => ({
    sleepTracking: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchSleepEntries();
  },
  methods: {
    fetchSleepEntries: function () {
      axios.get("/api/sleepTracking")
          .then(res => this.sleepTracking = res.data)
          .catch(() => alert("Error while fetching sleep entries"));
    },
    deleteSleepEntry: function (sleepEntry, index) {
      if (confirm('Are you sure you want to delete this sleep entry? This action cannot be undone.', 'Warning')) {
        const sleepEntryId = sleepEntry.id;
        const url = `/api/sleepTracking/${sleepEntryId}`;
        axios.delete(url)
            .then(response =>
                this.sleepTracking.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addSleepEntry: function () {
      const url = "/api/sleepTracking";
      axios.post(url, this.formData)
          .then(response => {
            this.sleepTracking.push(response.data);
            this.hideForm = true;
            // Reset form data after adding sleep entry
            this.formData = {
              quality: '',
              duration: null,
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
