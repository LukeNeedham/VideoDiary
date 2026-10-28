This app is distributed on F-Droid. To make a new release:

1. Add a changelog file for the new version code: [metadata/en-US/changelogs](../metadata/en-US/changelogs)
2. Maybe update the other metadata for any new features
3. Bump version name and code in Gradle, commit, tag the commit with the version name, then push
4. Make a signed apk build of the app at this tag. This requires the official app keystore, so F-Droid can check for reproducability
5. On Github create a new release from the latest tag, and upload the signed apk

You're done! The F-Droid CI should now handle publishing a new release automatically 

## Tester builds

To send a build to someone to try out, run the "Tester Release" workflow: add the `tester-build` label to the pull request to build its branch, or use Actions > Tester Release > Run workflow once the workflow is on `main`.
It builds the `beta` build type: signed with the debug keystore, with the application ID suffix `.beta`, and with `BuildConfig.DEBUG` false,
so it has none of the debug-only features (debug settings, fake SD card). It does save logs and crashes, with "Logs" and "Crashes" entries in Settings
so testers can send them back (`BuildConfig.DEBUG_KIT_ENABLED`, true in debug and beta builds, false in release builds). It publishes the APK as a GitHub pre-release,
which the "Delete pre-releases" workflow removes with the others. Locally: `./gradlew assembleBeta`.
Its launcher name is "Video Diary - Beta" (`app/src/beta/res`), like "Video Diary - Debug" for debug builds, so it is easy to tell apart from the real app.
