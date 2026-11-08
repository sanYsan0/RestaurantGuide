# Personal Restaurant Guide — Must-Have Requirements Update

This project was updated to focus on the Must Have functional requirements in the COMP 3074 proposal.

## Included functionality

- Splash screen with the application logo.
- Create and persist restaurant records using Room.
- Store restaurant name, address, phone number, description, tags, and a 1–5 rating.
- Add custom tags while creating or editing a restaurant.
- Display saved restaurants.
- Search saved restaurants by restaurant name or tag.
- Open a restaurant details screen.
- Edit all saved restaurant information.
- Delete a restaurant after confirmation.
- Change and save a restaurant's 1–5 star rating.
- Share restaurant information through an email app.
- Open a full-screen map for the saved address and request directions from the device's current location through a maps app.
- About screen listing all team members: Isaac Natera, Judene Brown, and Sanyoung Yoon.

## Excluded scope

Optional/Could Have and Won't Have features are not exposed in the app, including social-media sharing, price ranges, sorting, restaurant calling, optional themes, owner/manager dashboards, online reservations, and backend-server functionality.

## Technical notes

- Kotlin, Jetpack Compose, Room/SQLite, and MVVM remain in use.
- Map viewing and directions are handed off to an installed maps app using Android intents; no embedded map API key is required.
- Restaurant data is stored locally on the device.
- The minimum Android version is set to Android 8.0 (API 26), as stated in the proposal.

## Build verification

A build was attempted in this environment, but Gradle could not download its required distribution because network DNS access to `services.gradle.org` was unavailable. The source therefore still needs to be synced and built in Android Studio on a machine with the Android SDK and Gradle distribution available.
