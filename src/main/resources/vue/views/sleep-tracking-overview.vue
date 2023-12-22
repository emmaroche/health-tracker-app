<template id="sleep-tracking-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">Sleep Tracking</div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-sm" @click="hideForm =!hideForm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-plus" aria-hidden="true"></i> Add
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
          <!-- Add other form fields as needed -->
        </form>
        <button rel="tooltip" title="Add Sleep Entry" class="btn btn-info btn-sm" @click="addSleepEntry" style="background-color: #08a29e; border-color: #08a29e;">
          <i class="fa fa-plus" aria-hidden="true"></i> Add Sleep Entry
        </button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(sleepEntry, index) in sleepTracking" :key="index">
        <div class="mr-auto p-2">
          <span><a :href="`/sleepTracking/${sleepEntry.id}`" style="color: #08a29e;">
            {{ sleepEntry.quality }} (Duration: {{ sleepEntry.duration }} hours, Notes: {{ sleepEntry.notes }}, Date: {{ sleepEntry.date }}, User ID: {{ sleepEntry.userId }})
          </a></span>
        </div>
        <div class="p-2">
          <a :href="`/sleepTracking/${sleepEntry.id}`" class="btn btn-info btn-sm" style="background-color: #08a29e; border-color: #08a29e;">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm ml-2" @click="deleteSleepEntry(sleepEntry, index)">
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
