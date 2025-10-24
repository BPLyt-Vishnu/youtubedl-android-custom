package com.youtubedownloader.app.data

/**
 * Sealed class representing different download states
 */
sealed class DownloadState {
    object Idle : DownloadState()
    object FetchingInfo : DownloadState()
    data class InfoFetched(val qualities: List<VideoQuality>) : DownloadState()
    data class Downloading(val progress: Int, val speed: String?, val eta: String?) : DownloadState()
    data class Completed(val filePath: String) : DownloadState()
    data class Error(val message: String) : DownloadState()
}

/**
 * Data class for download progress
 */
data class DownloadProgress(
    val progress: Int,
    val speed: String? = null,
    val eta: String? = null,
    val totalSize: String? = null,
    val downloadedSize: String? = null
)
