package com.youtubedownloader.app.utils

import java.util.regex.Pattern

/**
 * Utility class for validating YouTube URLs
 */
object YouTubeUrlValidator {
    
    private val YOUTUBE_PATTERNS = listOf(
        Pattern.compile("(?:https?://)?(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_-]{11})"),
        Pattern.compile("(?:https?://)?(?:www\\.)?youtu\\.be/([a-zA-Z0-9_-]{11})"),
        Pattern.compile("(?:https?://)?(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_-]{11})"),
        Pattern.compile("(?:https?://)?(?:www\\.)?youtube\\.com/v/([a-zA-Z0-9_-]{11})"),
        Pattern.compile("(?:https?://)?(?:www\\.)?youtube\\.com/shorts/([a-zA-Z0-9_-]{11})")
    )
    
    /**
     * Validates if the given URL is a valid YouTube URL
     */
    fun isValidYouTubeUrl(url: String): Boolean {
        if (url.isBlank()) return false
        
        return YOUTUBE_PATTERNS.any { pattern ->
            pattern.matcher(url.trim()).find()
        }
    }
    
    /**
     * Extracts video ID from YouTube URL
     */
    fun extractVideoId(url: String): String? {
        if (!isValidYouTubeUrl(url)) return null
        
        for (pattern in YOUTUBE_PATTERNS) {
            val matcher = pattern.matcher(url.trim())
            if (matcher.find()) {
                return matcher.group(1)
            }
        }
        return null
    }
    
    /**
     * Normalizes YouTube URL to standard format
     */
    fun normalizeUrl(url: String): String {
        val videoId = extractVideoId(url) ?: return url
        return "https://www.youtube.com/watch?v=$videoId"
    }
}
