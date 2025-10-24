package com.youtubedownloader.app.service

import android.content.Context
import com.github.yausername.youtubedl_android.YoutubeDL
import com.github.yausername.youtubedl_android.YoutubeDLRequest
import com.github.yausername.youtubedl_android.YoutubeDLResponse
import com.youtubedownloader.app.data.VideoQuality
import com.youtubedownloader.app.data.DownloadState
import com.youtubedownloader.app.utils.StorageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service class for handling YouTube video downloads
 */
@Singleton
class YouTubeDownloadService @Inject constructor() {
    
    /**
     * Fetches video information and available qualities
     */
    suspend fun fetchVideoInfo(url: String): Flow<DownloadState> = flow {
        try {
            emit(DownloadState.FetchingInfo)
            
            withContext(Dispatchers.IO) {
                val request = YoutubeDLRequest(url)
                request.addOption("--dump-json")
                request.addOption("--no-playlist")
                
                val response = YoutubeDL.getInstance().getInfo(request)
                val jsonResponse = response.out
                
                if (jsonResponse.isNotEmpty()) {
                    val videoInfo = JSONObject(jsonResponse)
                    val formats = videoInfo.getJSONArray("formats")
                    val qualities = parseVideoQualities(formats, videoInfo)
                    
                    emit(DownloadState.InfoFetched(qualities))
                } else {
                    emit(DownloadState.Error("No video information found"))
                }
            }
        } catch (e: Exception) {
            emit(DownloadState.Error("Error fetching video info: ${e.message}"))
        }
    }
    
    /**
     * Downloads video with specified quality
     */
    suspend fun downloadVideo(
        context: Context,
        url: String,
        quality: VideoQuality,
        onProgress: (Int, String?, String?) -> Unit
    ): Flow<DownloadState> = flow {
        try {
            withContext(Dispatchers.IO) {
                val downloadDir = StorageUtils.createDownloadDirectory(context)
                val request = YoutubeDLRequest(url)
                
                // Set output path
                request.addOption("-o", "${downloadDir.absolutePath}/%(title)s.%(ext)s")
                
                // Set format
                if (quality.isAudioOnly) {
                    request.addOption("-f", "bestaudio")
                    request.addOption("--extract-audio")
                    request.addOption("--audio-format", "mp3")
                } else {
                    request.addOption("-f", quality.formatId)
                }
                
                // Add progress callback
                request.addOption("--progress", "true")
                
                val response = YoutubeDL.getInstance().execute(request) { progress, speed, eta ->
                    onProgress(progress, speed, eta)
                }
                
                if (response.exitCode == 0) {
                    val outputPath = response.out.trim()
                    emit(DownloadState.Completed(outputPath))
                } else {
                    emit(DownloadState.Error("Download failed: ${response.err}"))
                }
            }
        } catch (e: Exception) {
            emit(DownloadState.Error("Download error: ${e.message}"))
        }
    }
    
    /**
     * Parses video qualities from YouTube-DL response
     */
    private fun parseVideoQualities(formats: org.json.JSONArray, videoInfo: JSONObject): List<VideoQuality> {
        val qualities = mutableListOf<VideoQuality>()
        
        // Add audio-only option
        qualities.add(
            VideoQuality(
                formatId = "bestaudio",
                height = null,
                width = null,
                fps = null,
                vcodec = null,
                acodec = "mp3",
                ext = "mp3",
                filesize = null,
                formatNote = "Audio Only",
                isAudioOnly = true
            )
        )
        
        // Parse video formats
        for (i in 0 until formats.length()) {
            val format = formats.getJSONObject(i)
            
            try {
                val formatId = format.optString("format_id", "")
                val height = format.optInt("height", 0).takeIf { it > 0 }
                val width = format.optInt("width", 0).takeIf { it > 0 }
                val fps = format.optInt("fps", 0).takeIf { it > 0 }
                val vcodec = format.optString("vcodec", "none")
                val acodec = format.optString("acodec", "none")
                val ext = format.optString("ext", "mp4")
                val filesize = format.optLong("filesize", 0).takeIf { it > 0 }
                val formatNote = format.optString("format_note", "")
                
                // Only include formats with video
                if (vcodec != "none" && vcodec.isNotEmpty()) {
                    qualities.add(
                        VideoQuality(
                            formatId = formatId,
                            height = height,
                            width = width,
                            fps = fps,
                            vcodec = vcodec,
                            acodec = acodec,
                            ext = ext,
                            filesize = filesize,
                            formatNote = formatNote,
                            isAudioOnly = false
                        )
                    )
                }
            } catch (e: Exception) {
                // Skip invalid formats
                continue
            }
        }
        
        // Sort by height (descending) and remove duplicates
        return qualities
            .distinctBy { "${it.height}_${it.ext}" }
            .sortedByDescending { it.height ?: 0 }
    }
}
