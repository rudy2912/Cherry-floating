# Cherry Floating Pet (Android)

A small native Android starter app for a true always-on-top draggable mascot overlay.

## Build
1. Open this folder in Android Studio on a computer.
2. Let Gradle sync and install the Android SDK platform 35 if prompted.
3. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Install the generated debug APK on your Android tablet.

## Use on the tablet
1. Open Cherry Floating Pet.
2. Tap **Allow floating permission** and enable **Allow display over other apps**.
3. Return to Cherry and tap **Start Cherry**.
4. Cherry appears above other apps. Drag her to move; tap her to change expression.
5. Use the persistent notification's **Stop Cherry** action to dismiss her.

## Notes
- This is a starter build, not a signed Play Store release.
- Android may ask for notification permission on newer versions.
- Android 14+ applies foreground-service rules. The app declares the special-use subtype for a user-started floating pet; device/OS policy may still affect background operation.
- The mascot is currently drawn in code as a simple placeholder. The next iteration can replace it with your finalized Cherry artwork and add animation, feeding, sleep, and music reactions.
