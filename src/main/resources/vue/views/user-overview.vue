<template id="user-overview">
  <app-layout>
    <div class="card bg-light mt-4 mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6" style="font-weight: 600;">
            Users
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-sm" @click="hideForm = !hideForm" style="background-color: #08a29e; border-color: #08a29e;">
              <i class="fa fa-plus" aria-hidden="true"></i> Add
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addUser">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-name">Username</span>
            </div>
            <input type="text" class="form-control" v-model="formData.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="formData.email" name="email" placeholder="Email"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-phone">Phone Number</span>
            </div>
            <input type="number" class="form-control" v-model="formData.phoneNumber" name="phoneNumber" placeholder="Phone Number"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text custom-label" style="font-weight: 600;" id="input-user-address">Address</span>
            </div>
            <input type="text" class="form-control" v-model="formData.address" name="address" placeholder="Address"/>
          </div>
        </form>
        <button rel="tooltip" title="Add" class="btn btn-info btn-sm mt-3" @click="addUser" style="background-color: #08a29e; border-color: #08a29e;">
          <i class="fa fa-plus" aria-hidden="true"></i> Add
        </button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(user, index) in users" :key="index">
        <div class="mr-auto p-2">
          <p><strong>Username:</strong> {{ user.name }}</p>
          <p><strong>Email:</strong> {{ user.email }}</p>
          <p><strong>Phone:</strong> {{ user.phoneNumber }}</p>
          <p><strong>Address:</strong> {{ user.address }}</p>
          <a :href="`/users/${user.id}`" style="color: #08a29e;">
            View Details
          </a>
        </div>
        <div class="ml-auto p-2">
          <a :href="`/users/${user.id}`" class="btn btn-info btn-sm mr-2" style="background-color: #08a29e; border-color: #08a29e;">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-danger btn-sm" @click="deleteUser(user, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
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
app.component("user-overview", {
  template: "#user-overview",
  data: () => ({
    users: [],
    formData: {},
    hideForm: true,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("We couldn't find any users at the moment. Feel free to add a new user, wait a moment, or refresh the page to check again"));
    },
    deleteUser: function (user, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
        const userId = user.id;
        const url = `/api/users/${userId}`;
        axios.delete(url)
            .then(response => this.users.splice(index, 1).push(response.data))
            .catch(error => console.log(error));
      }
    },
    addUser: function () {
      const url = "/api/users";
      axios.post(url, this.formData)
          .then(response => {
            this.users.push(response.data);
            this.hideForm = true;
            // Reset form data after adding user
            this.formData = {
              name: '',
              email: null,
              phoneNumber: null,
              address: null,
            };
          })
          .catch(error => {
            console.error('Error adding user:', error);
          });
    },
  }
});
</script>

