## App Overview

This Web Application is designed to help users track their health goals. 

The application is developed using Kotlin, with data storage handled by an ElephantSQL database. Testing is carried out using JUnit 5 and Unirest, and the app is hosted on Railway at https://health-tracker-app-production.up.railway.app.

## App Functionality

### Core Features

Some of these features are linked to eachother through their corresponding profile pages.

1. **User and Activities:**
   - Designed features for user information and activity tracking.
   - Created API endpoints for managing user profiles and recording activities.
   - Ensured the API endpoints support CRUD operations for user data and activities.

2. **Health Goal Tracking - Fitness, Nutrtion and Weight Goals:**
   - Implemented fitness, nutrtion and weight goals tracker features.
   - Created API endpoints for tracking fitness goals, nutrtion goals and weight goals.
   - Implemented the ability to add, update, delete, and retrieve fitness, nutrtion and weight goals.
   - Ensured the API endpoints support CRUD operations for these goals.

3. **Health Records:**
   - Created API endpoints for health records.
   - Implemented the ability to add, update, delete, and retrieve health records.
   - Ensured the API endpoints support CRUD operations for health records.

4. **Sleep Tracking:**
   - Added sleep tracking functionality.
   - Created API endpoints for tracking sleep data.
   - Implemented the ability to add, update, delete, and retrieve sleep data.
   - Ensured the API endpoints support CRUD operations for sleep tracking.

5. **Mood Tracking:**
   - Incorporated mood tracking features.
   - Created API endpoints for recording mood data.
   - Implemented the ability to add, update, delete, and retrieve mood data.
   - Ensured the API endpoints support CRUD operations for mood tracking.

6. **User Weight:**
   - Included user weight tracking functionality.
   - Created API endpoints for tracking user weight.
   - Implemented the ability to add, update, delete, and retrieve user weight data.
   - Ensured the API endpoints support CRUD operations for user weight.

### Testing of Core Features

- **Unit and Integration Tests:**
   - Wrote JUnit tests to verify the functionality of the application's core features.
   - Included unit tests for individual components and integration tests to test the interaction between components.

## GitHub Workflow

**The GitHub workflow used for the development of this app is as follows:**

Issue - Branch - Commit(s) - Push - Pull Request - Merge - Close Issue - Delete Branch
