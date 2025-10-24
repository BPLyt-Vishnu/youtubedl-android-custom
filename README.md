# YouTube Downloader Android App

A modern, fully functional Android app for downloading YouTube videos with quality selection, built using Kotlin and Jetpack Compose.

## Features

- **YouTube Video Downloading**: Download videos from any YouTube URL
- **Quality Selection**: Choose from available video qualities (144p, 360p, 720p, 1080p, etc.)
- **Audio-Only Downloads**: Download audio in MP3 format
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **Progress Tracking**: Real-time download progress with speed and ETA
- **Error Handling**: Comprehensive error handling for various scenarios
- **Storage Management**: Automatic file organization in Downloads folder
- **Legal Compliance**: Built-in disclaimer and ToS compliance

## Screenshots

The app features a clean, modern interface with:
- URL input field with validation
- Quality selection dropdown
- Download progress indicators
- Error and success notifications
- Legal disclaimer dialog

## Technical Requirements

### Minimum Requirements
- **Android API Level**: 28 (Android 9.0 Pie)
- **Target API Level**: 35 (Android 15)
- **Kotlin Version**: 1.9.10
- **Gradle Version**: 8.4
- **Android Gradle Plugin**: 8.1.4

### Dependencies
- **YouTube-DL Android**: For video downloading and parsing
- **FFmpeg**: For video/audio merging (included)
- **Jetpack Compose**: Modern UI toolkit
- **Hilt**: Dependency injection
- **Coroutines**: Asynchronous operations
- **WorkManager**: Background downloads

## Setup Instructions

### 1. Prerequisites

Before setting up the project, ensure you have:

- **Android Studio**: Latest version (Hedgehog or newer)
- **Android SDK**: API 21-34 installed
- **Java Development Kit**: JDK 8 or higher
- **Git**: For version control (optional)

### 2. Project Setup

1. **Clone or Download the Project**
   ```bash
   git clone <repository-url>
   cd YouTubeDownloader
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the project folder and select it
   - Wait for Gradle sync to complete

3. **Sync Project**
   - Android Studio will automatically sync the project
   - If sync fails, go to `File > Sync Project with Gradle Files`
   - Wait for all dependencies to download

### 3. Configuration

#### Build Configuration
The project is pre-configured with:
- **Application ID**: `com.youtubedownloader.app`
- **Package Name**: `com.youtubedownloader.app`
- **Version Code**: 1
- **Version Name**: "1.0"
- **Min SDK**: 28 (Android 9.0 Pie)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35 (Android 15)

#### Permissions
The app automatically requests necessary permissions:
- `INTERNET` - For downloading videos
- `WRITE_EXTERNAL_STORAGE` - For saving files (Android 9-10)
- `READ_EXTERNAL_STORAGE` - For accessing files (Android 9-10)
- `MANAGE_EXTERNAL_STORAGE` - For Android 11+ storage access

### 4. Building and Running

#### Debug Build
1. Connect an Android device or start an emulator
2. Click the "Run" button (green play icon) in Android Studio
3. Select your target device
4. Wait for the app to install and launch

#### Release Build
1. Go to `Build > Generate Signed Bundle/APK`
2. Create a new keystore or use existing one
3. Select "APK" or "Android App Bundle"
4. Choose release build type
5. Click "Create"

### 5. Testing

#### Device Testing
- Test on various Android versions (9.0+)
- Test with different YouTube video types:
  - Regular videos
  - Shorts
  - Live streams
  - Age-restricted content
  - Region-locked content

#### Emulator Testing
- Use Android Studio's built-in emulator
- Test different screen sizes and densities
- Test different Android API levels

## Project Structure

```
app/
├── src/main/java/com/youtubedownloader/app/
│   ├── data/
│   │   ├── VideoQuality.kt          # Video quality data model
│   │   └── DownloadState.kt        # Download state management
│   ├── service/
│   │   └── YouTubeDownloadService.kt # Core download functionality
│   ├── ui/
│   │   ├── YouTubeDownloadScreen.kt # Main UI composables
│   │   ├── YouTubeDownloadViewModel.kt # ViewModel for UI state
│   │   └── theme/                  # Material Design theme
│   ├── utils/
│   │   ├── YouTubeUrlValidator.kt  # URL validation utilities
│   │   └── StorageUtils.kt         # Storage management utilities
│   ├── MainActivity.kt             # Main activity
│   └── YouTubeDownloaderApplication.kt # Application class
├── src/main/res/
│   ├── drawable/                   # App icons and graphics
│   ├── mipmap/                     # App launcher icons
│   ├── values/                     # Strings, colors, themes
│   └── xml/                        # Configuration files
└── build.gradle                    # App-level build configuration
```

## Key Components

### 1. YouTubeDownloadService
- Handles YouTube video information fetching
- Manages video quality parsing
- Executes downloads with progress tracking
- Integrates with YouTube-DL Android library

### 2. YouTubeDownloadViewModel
- Manages UI state and user interactions
- Coordinates between UI and download service
- Handles error states and success notifications
- Implements MVVM architecture pattern

### 3. YouTubeDownloadScreen
- Jetpack Compose UI implementation
- Material Design 3 components
- Responsive layout for different screen sizes
- Real-time progress updates

### 4. Storage Management
- Automatic download directory creation
- File naming with timestamps
- Storage permission handling
- Android 11+ scoped storage compliance

## Usage Guide

### Basic Usage
1. **Launch the App**: Open YouTube Downloader from your app drawer
2. **Enter URL**: Paste a YouTube video URL in the input field
3. **Fetch Qualities**: Tap "Fetch Qualities" to get available options
4. **Select Quality**: Choose your preferred video quality
5. **Download**: Tap the quality option to start downloading
6. **Monitor Progress**: Watch the download progress and ETA

### Supported URL Formats
- `https://www.youtube.com/watch?v=VIDEO_ID`
- `https://youtu.be/VIDEO_ID`
- `https://www.youtube.com/embed/VIDEO_ID`
- `https://www.youtube.com/shorts/VIDEO_ID`

### Quality Options
- **Video Qualities**: 144p, 360p, 720p, 1080p, 4K (when available)
- **Audio Only**: MP3 format for audio-only downloads
- **Format Information**: Shows codec, file size, and other details

## Troubleshooting

### Common Issues

#### 1. Build Errors
- **Gradle Sync Failed**: Clean and rebuild project
- **Dependency Issues**: Check internet connection and try again
- **SDK Issues**: Update Android SDK in SDK Manager

#### 2. Runtime Errors
- **Permission Denied**: Grant storage permissions in Settings
- **Network Errors**: Check internet connection
- **Download Failures**: Try different video quality or URL

#### 3. Performance Issues
- **Slow Downloads**: Check network speed and video size
- **App Crashes**: Check device storage space
- **Memory Issues**: Close other apps to free up RAM

### Debug Mode
Enable debug logging by adding to `build.gradle`:
```gradle
android {
    buildTypes {
        debug {
            debuggable true
            minifyEnabled false
        }
    }
}
```

## Legal and Ethical Considerations

### Important Disclaimers
- **Personal Use Only**: This app is for personal use only
- **YouTube ToS Compliance**: Users must comply with YouTube's Terms of Service
- **Copyright Respect**: Do not download copyrighted content without permission
- **Regional Restrictions**: Respect regional content restrictions

### Content Restrictions
The app will not download:
- Age-restricted content without proper authentication
- Region-locked content
- Copyright-protected content (when detected)
- Live streams (limited support)

## Contributing

### Development Setup
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Maintain consistent formatting

## License

This project is for educational and personal use only. Users are responsible for complying with all applicable laws and terms of service.

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review the code comments
3. Test with different devices and Android versions
4. Ensure all dependencies are properly installed

## Future Enhancements

Potential improvements for future versions:
- Batch download support
- Download queue management
- Custom output directory selection
- Video thumbnail previews
- Download history
- Playlist support
- Subtitle download options

---

## 🎯 **App Built by Vishnu Mahawar**

**Developed with ❤️ by [Vishnu Mahawar](https://github.com/vishnumahawar)**

This YouTube Downloader app was meticulously crafted by Vishnu Mahawar, featuring:
- ✨ Modern Jetpack Compose UI with Material Design 3
- 🚀 Advanced YouTube video downloading capabilities
- 📱 Support for Android 9 (API 28) to Android 15 (API 35)
- 🛡️ Comprehensive error handling and user experience
- 📋 Clean architecture with MVVM pattern
- 🔧 Robust Gradle configuration with latest dependencies

**Connect with the Developer:**
- GitHub: [@vishnumahawar](https://github.com/vishnumahawar)
- LinkedIn: [Vishnu Mahawar](https://linkedin.com/in/vishnumahawar)
- Email: vishnumahawar.dev@gmail.com

---

**Note**: This app is designed for educational purposes and personal use. Always respect copyright laws and platform terms of service when downloading content.
