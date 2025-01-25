This app is distributed on F-Droid. To make a new release:

1. Add a changelog file for the new version code: [metadata/en-US/changelogs](../metadata/en-US/changelogs)
2. Maybe update the other metadata for any new features
3. Bump version name and code in Gradle, commit, tag the commit with the version name, then push
4. Make a signed apk build of the app at this tag. This requires the official app keystore, so F-Droid can check for reproducability
5. On Github create a new release from the latest tag, and upload the signed apk

You're done! The F-Droid CI should now handle publishing a new release automatically 
