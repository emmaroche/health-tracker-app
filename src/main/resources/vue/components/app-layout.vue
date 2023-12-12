<template id="app-layout">
  <div class="app-layout">
    <div class="container">
      <!-- Start of navbar -->
      <nav class="navbar navbar-expand-lg navbar-light">
        <a class="navbar-brand" href="/">Home</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse"
                data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item" :class="{ 'active': activeLink === '/users' }">
              <a @click="setActiveLink('/users')" class="nav-link">
                Users
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/activities' }">
              <a @click="setActiveLink('/activities')" class="nav-link">
                Activities
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/fitnessGoals' }">
              <a @click="setActiveLink('/fitnessGoals')" class="nav-link">
                Fitness Goals
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/weightGoals' }">
              <a @click="setActiveLink('/weightGoals')" class="nav-link">
                Weight Goals
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/healthRecords' }">
              <a @click="setActiveLink('/healthRecords')" class="nav-link">
                Health Records
              </a>
            </li>
          </ul>
        </div>
      </nav>
      <!--End of nav bar-->
      <!--Start of main content area-->
      <div class="content mt-3">
        <div class="container-fluid">
          <slot></slot>
        </div>
      </div>
      <!--End of main content area-->
    </div>
  </div>
</template>

<script>
app.component("app-layout", {
  template: "#app-layout",
  data() {
    return {
      activeLink: null
    };
  },
  mounted() {
    // Retrieve active link from local storage on component mount
    this.activeLink = localStorage.getItem('activeLink') || null;
  },
  methods: {
    setActiveLink(link) {
      this.activeLink = link;
      // Save active link to local storage
      localStorage.setItem('activeLink', link);
      // Manual redirect
      window.location.href = link;
    }
  }
});
</script>

<style>
.navbar {
  background-color: #e3f2fd;
}

.nav-item.active {
  color: #bbdefb; /* Highlight color for active link */
}
</style>
