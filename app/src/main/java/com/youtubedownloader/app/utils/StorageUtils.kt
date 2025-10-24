package com.youtubedownloader.app.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

/**
 * Utility class for handling storage operations
 */
object StorageUtils {
    
    /**
     * Gets the default download directory
     */
    fun getDefaultDownloadDirectory(context: Context): File {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use app-specific external storage for Android 10+
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "YouTubeDownloads")
        } else {
            // Use public Downloads directory for Android 9
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "YouTubeDownloads")
        }
    }
    
    /**
     * Creates download directory if it doesn't exist
     */
    fun createDownloadDirectory(context: Context): File {
        val downloadDir = getDefaultDownloadDirectory(context)
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }
        return downloadDir
    }
    
    /**
     * Generates a unique filename for the download
     */
    fun generateFileName(title: String, extension: String): String {
        // Clean the title to make it filesystem-safe
        val cleanTitle = title.replace(Regex("[^a-zA-Z0-9._-]"), "_")
            .replace(Regex("_{2,}"), "_")
            .trim('_')
        
        val timestamp = System.currentTimeMillis()
        return "${cleanTitle}_$timestamp.$extension"
    }
    
    /**
     * Checks if external storage is available
     */
    fun isExternalStorageAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
    
    /**
     * Gets available storage space in bytes
     */
    fun getAvailableStorageSpace(context: Context): Long {
        val downloadDir = getDefaultDownloadDirectory(context)
        return downloadDir.usableSpace
    }
}
