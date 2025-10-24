package com.youtubedownloader.app.data

/**
 * Data class representing a video quality option
 */
data class VideoQuality(
    val formatId: String,
    val height: Int?,
    val width: Int?,
    val fps: Int?,
    val vcodec: String?,
    val acodec: String?,
    val ext: String,
    val filesize: Long?,
    val formatNote: String?,
    val isAudioOnly: Boolean = false
) {
    /**
     * Get display name for the quality option
     */
    fun getDisplayName(): String {
        return when {
            isAudioOnly -> "Audio Only (${ext.uppercase()})"
            height != null -> "${height}p"
            width != null -> "${width}x${height ?: "?"}"
            else -> formatNote ?: formatId
        }
    }
    
    /**
     * Get detailed description
     */
    fun getDescription(): String {
        val parts = mutableListOf<String>()
        
        if (height != null) parts.add("${height}p")
        if (fps != null) parts.add("${fps}fps")
        if (vcodec != null) parts.add("Video: $vcodec")
        if (acodec != null) parts.add("Audio: $acodec")
        if (filesize != null) parts.add("Size: ${formatFileSize(filesize)}")
        
        return parts.joinToString(" • ")
    }
    
    private fun formatFileSize(bytes: Long): String {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        
        return when {
            gb >= 1 -> "%.1f GB".format(gb)
            mb >= 1 -> "%.1f MB".format(mb)
            else -> "%.1f KB".format(kb)
        }
    }
}
