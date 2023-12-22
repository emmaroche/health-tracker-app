<template id="app-layout">
  <div class="app-layout">
    <!-- Start of navbar -->
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container text-center">
<!--        <a class="navbar-brand" :class="{ 'active': activeLink === '/' }" @click="setActiveLink('/')">Home</a>-->
        <button class="navbar-toggler" type="button" data-toggle="collapse"
                data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mx-auto">
            <li class="nav-item" :class="{ 'active': activeLink === '/' }">
              <a @click="setActiveLink('/')" class="nav-link">
                Home
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/' }">
              <a @click="setActiveLink('/')" class="nav-link">
                Dashboard
              </a>
            </li>
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
            <li class="nav-item" :class="{ 'active': activeLink === '/nutritionGoals' }">
              <a @click="setActiveLink('/nutritionGoals')" class="nav-link">
                Nutrition Goals
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/healthRecords' }">
              <a @click="setActiveLink('/healthRecords')" class="nav-link">
                Health Records
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/sleepTracking' }">
              <a @click="setActiveLink('/sleepTracking')" class="nav-link">
                Sleep Tracking
              </a>
            </li>
            <li class="nav-item" :class="{ 'active': activeLink === '/moodTracking' }">
              <a @click="setActiveLink('/moodTracking')" class="nav-link">
                Mood Tracking
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <!--End of nav bar-->

    <!--Start of main content area-->
    <div class="container mt-3">
      <slot></slot>
    </div>
    <!--End of main content area-->
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

  //ChatGPT generated active link code

  mounted() {
    // Retrieve active link from local storage on component mount
    this.activeLink = localStorage.getItem('activeLink') || null;
  },
  methods: {
    setActiveLink(link) {
      if (this.activeLink === link) {
        // If the clicked link is already active, unhighlight it
        this.activeLink = null;
      } else {
        this.activeLink = link;
        // Save active link to local storage
        localStorage.setItem('activeLink', link);
        // Manual redirect
        window.location.href = link;
      }
    }
  }
});
</script>

<style>
.navbar {
  background-color: #08a29e;
  color: white;
}

.navbar-nav .nav-item.active a,
.navbar-brand.active {
  color: #000000 !important;
}

.navbar-nav .nav-link {
  color: white !important;
  font-size: 18px; /* Adjust the font size to your preference */
}

.app-layout {
  width: 100%;
  max-width: 100%;
  margin: 0;
  padding: 0;
}
</style>



