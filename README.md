# Android App (JBAC Mobile) 

This Android app is a fully offline-first Compose app with the following sections:
- Dashboard (home summary and highlights)
- Notices (search + important filter)
- Events (search)
- Contact (form validation)
- About

## Open In Android Studio

1. Open Android Studio.
2. Select **Open**.
3. Choose this folder: `android-app`.
4. Wait for Gradle sync to complete.

## Requirements

- Android Studio (recent stable version)
- Android SDK for API 35
- JDK 17 (Android Studio usually manages this)

## Run The App

1. Open **Device Manager** and start an emulator (for example Pixel + Android 14/15 image), or connect a physical Android device.
2. Select the **app** run configuration.
3. Click **Run**.

## Manual Feature Checklist

1. Dashboard
- App opens to Dashboard.
- Title, tagline, highlights, and stats are visible.

2. Notices
- Go to Notices tab.
- Search by title/category and verify list filtering.
- Toggle **Important only** and verify only important notices remain.

3. Events
- Go to Events tab.
- Search by event name/venue/description and verify filtering.

4. Contact
- Go to Contact tab.
- Submit invalid email and confirm validation error.
- Submit message shorter than 10 chars and confirm validation error.
- Submit valid input and confirm success state.

5. About
- Go to About tab and verify app summary and features list.

## Notes

- This module currently does not include a committed Gradle wrapper (`gradlew`).
- Build and run from Android Studio works via Gradle sync.
- If you want CLI builds, generate wrapper once:
  - In Android Studio terminal at `android-app`: `gradle wrapper`
  - Then use: `./gradlew :app:assembleDebug`

## Render.com deployment notes

- Render's Docker service requires a root `Dockerfile`; this repo is Android native.
- Added `Dockerfile` + `index.html` as a fallback static page so Docker build no longer fails.
- For a real Android release pipeline, prefer Google Play / Firebase App Distribution rather than Render web service.

