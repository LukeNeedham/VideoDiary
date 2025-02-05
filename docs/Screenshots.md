The screenshots used in F-Droid are found here: [metadata/en-US/images/phoneScreenshots](../metadata/en-US/images/phoneScreenshots)

These screenshots are taken from Composable Previews of top-level Pages.

These Previews use the drawable `preview_video` to render an interesting image as the placeholder for video players.
This drawable is a large PNG file, so we want to exclude it from release apk builds.
For this reason, this drawable lives under `debug/res/drawable`.

In order to allow builds to succeed, we need to also provide a fallback `preview_video` drawable for release builds, even though we know it will never be used. For this reason, the default drawable folder also contains a simple sample `preview_video`.

When making screenshots for metadata, make sure the build variant is set to debug, so that the proper preview image is used.