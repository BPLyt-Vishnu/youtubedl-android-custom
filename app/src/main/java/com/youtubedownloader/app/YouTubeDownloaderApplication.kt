package com.youtubedownloader.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for YouTube Downloader app
 * Initializes Hilt dependency injection
 */
@HiltAndroidApp
class YouTubeDownloaderApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize YouTube-DL
        try {
            com.github.yausername.youtubedl_android.YoutubeDL.getInstance().init(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
