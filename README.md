# Jarvis AI Assistant App

A comprehensive Android application that creates an AI assistant named Jarvis with advanced capabilities including accessibility service integration, text-to-speech functionality, and notification listening.

## Features

### 1. **TextToSpeech (TTS)**
- Converts text to speech with support for multiple languages (Hindi and English)
- Initialized from MainActivity with a "Sir, main online hoon" greeting
- Supports queue management for multiple speech requests

### 2. **AccessibilityService**
- UI automation capabilities
- Gesture-based control (scroll up, scroll down, tap)
- Uses `dispatchGesture()` for smooth gesture dispatch
- Receives commands via broadcast from MainActivity

### 3. **NotificationListenerService**
- Captures WhatsApp notifications in real-time
- Extracts sender name and message content
- Broadcasts received messages for further processing
- Supports both WhatsApp and WhatsApp Business apps

## Project Structure

```
Jarvis-AI/
├── .github/
│   └── workflows/
│       └── build.yml                 # CI/CD GitHub Actions workflow
├── app/
│   ├── src/main/
│   │   ├── java/com/jarvis/ai/
│   │   │   ├── MainActivity.kt        # Main activity with UI controls
│   │   │   ├── JarvisAccessibilityService.kt   # Accessibility service for gestures
│   │   │   └── JarvisNotificationService.kt    # Notification listener for messages
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   └── xml/
│   │   │       └── accessibility_config.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── .gitignore
└── README.md
```

## File Descriptions

### 1. `settings.gradle.kts`
- Gradle settings for project configuration
- Defines plugin management and dependency repositories

### 2. `build.gradle.kts` (Root)
- Root-level build configuration
- Defines AGP and Kotlin plugin versions

### 3. `app/build.gradle.kts`
- App-level build configuration
- Dependencies: AndroidX, Material Design, Coroutines, TTS
- Target SDK: 34, Min SDK: 24

### 4. `app/src/main/AndroidManifest.xml`
- Declares all required permissions:
  - `RECORD_AUDIO` - For speech recognition
  - `BIND_ACCESSIBILITY_SERVICE` - For accessibility service
  - `BIND_NOTIFICATION_LISTENER_SERVICE` - For notification listening
- Registers services and activities

### 5. `app/src/main/res/xml/accessibility_config.xml`
- Configuration for AccessibilityService
- Enables gesture dispatch capability
- Configures feedback types and event types

### 6. `app/src/main/res/values/strings.xml`
- String resources for UI elements
- Includes greeting message: "Sir, main online hoon"

### 7. `app/src/main/java/com/jarvis/ai/MainActivity.kt`
- Main user interface
- TTS initialization and control
- Button listeners for voice, scrolling, and service management
- Service status display
- Opens system settings for enabling services

### 8. `app/src/main/java/com/jarvis/ai/JarvisAccessibilityService.kt`
- Extends `AccessibilityService` for UI automation
- **Key Methods:**
  - `performScrollDown()` - Scroll down using gesture dispatch
  - `performScrollUp()` - Scroll up gesture
  - `performTap()` - Tap gesture at coordinates
- Uses `GestureDescription` API for smooth gestures

### 9. `app/src/main/java/com/jarvis/ai/JarvisNotificationService.kt`
- Extends `NotificationListenerService`
- Listens for all notifications
- Specifically handles WhatsApp messages
- Parses sender and message content
- Broadcasts messages for app-wide consumption

### 10. `.github/workflows/build.yml`
- GitHub Actions CI/CD workflow
- Runs on: `ubuntu-latest`
- **Steps:**
  1. Checkout repository
  2. Setup Java JDK 17
  3. Setup Android SDK
  4. Build debug APK
  5. Upload artifact with 30-day retention

## Setup Instructions

### Prerequisites
- Android Studio (latest version)
- JDK 17 or higher
- Android SDK (API level 34)
- Gradle 8.1.0 or higher

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/abce72673-rgb/Jarvis-AI.git
   cd Jarvis-AI
   ```

2. **Open in Android Studio:**
   - File → Open → Select the project directory

3. **Sync Gradle:**
   - Android Studio will automatically sync dependencies

4. **Enable Required Permissions:**
   - Build and run the app
   - Go to Settings → Accessibility → Enable "Jarvis AI"
   - Go to Settings → Notifications → Notification Access → Enable "Jarvis AI"

5. **Build APK:**
   ```bash
   ./gradlew assembleDebug
   ```

## Usage

### Initialize TextToSpeech
1. Tap "Initialize TTS" button
2. The app will initialize TextToSpeech engine
3. Status will show "TTS: ✓ Ready"

### Use Voice Output
1. Tap "Speak" button
2. Jarvis will say "Sir, main online hoon" in Hindi/English

### Perform Scrolling
1. Ensure Accessibility Service is enabled
2. Tap "Scroll Down" button
3. The app will simulate a scroll gesture on active app

### Enable Accessibility Service
1. Tap "Enable Accessibility Service" button
2. Go to Settings → Accessibility
3. Find and enable "Jarvis AI"

### Enable Notification Listener
1. Tap "Enable Notification Listener" button
2. Go to Settings → Notifications → Notification Access
3. Enable "Jarvis AI"
4. App will now receive WhatsApp notifications

## Permissions Required

| Permission | Purpose |
|-----------|---------|
| `RECORD_AUDIO` | Speech recognition and voice input |
| `BIND_ACCESSIBILITY_SERVICE` | UI automation and gestures |
| `BIND_NOTIFICATION_LISTENER_SERVICE` | Reading app notifications |
| `INTERNET` | Network connectivity |
| `MODIFY_AUDIO_SETTINGS` | Audio volume and settings control |

## Technical Details

### TextToSpeech Implementation
- Supports Hindi (hi_IN) and English (en_US)
- Uses Android's built-in TTS engine
- Proper resource cleanup in `onDestroy()`

### Accessibility Service Gestures
- Uses `GestureDescription.Builder` for gesture creation
- `dispatchGesture()` API for gesture execution
- Smooth 300ms gesture duration
- Callback-based gesture completion handling

### Notification Parsing
- Extracts notification extras (title, text, big text)
- Parses WhatsApp format: "Sender: Message"
- Broadcasts to other app components
- Handles both WhatsApp and WhatsApp Business

## GitHub Actions Workflow

The workflow automatically:
1. Triggers on push to main/master/develop branches
2. Triggers on pull requests to these branches
3. Builds debug APK using JDK 17
4. Uploads APK as artifact with 30-day retention

### Artifact Download
- Visit Actions tab on GitHub
- Select the workflow run
- Download "jarvis-ai-debug" artifact

## API Levels
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Compile SDK:** 34

## Dependencies
- androidx.core:core-ktx:1.12.0
- androidx.appcompat:appcompat:1.6.1
- com.google.android.material:material:1.10.0
- androidx.constraintlayout:constraintlayout:2.1.4
- org.jetbrains.kotlinx:kotlinx-coroutines:1.7.1
- androidx.lifecycle:lifecycle-runtime-ktx:2.6.2

## Troubleshooting

### TTS Not Initialized
- Install TTS data from Google Play Services
- Check device language settings
- Restart the app

### Accessibility Service Not Working
- Enable in Settings → Accessibility
- Grant all requested permissions
- Restart the device if issues persist

### Notifications Not Received
- Enable in Settings → Notifications → Notification Access
- Ensure app has notification permission
- Check app is not in restricted battery mode

## Contributing
Contributions are welcome! Please feel free to submit pull requests.

## License
This project is open source and available under the MIT License.

## Support
For issues or questions, please create an issue on GitHub.

---

**Note:** This app requires careful permission management and should be used responsibly. Some features require enabling special access that should only be granted to trusted apps.
