package com.youtubedownloader.app.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.youtubedownloader.app.data.DownloadState
import com.youtubedownloader.app.data.VideoQuality
import com.youtubedownloader.app.service.YouTubeDownloadService
import com.youtubedownloader.app.utils.YouTubeUrlValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for YouTube download functionality
 */
@HiltViewModel
class YouTubeDownloadViewModel @Inject constructor(
    application: Application,
    private val downloadService: YouTubeDownloadService
) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(YouTubeDownloadUiState())
    val uiState: StateFlow<YouTubeDownloadUiState> = _uiState.asStateFlow()
    
    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Idle)
    val downloadState: StateFlow<DownloadState> = _downloadState.asStateFlow()
    
    /**
     * Updates the YouTube URL input
     */
    fun updateUrl(url: String) {
        _uiState.value = _uiState.value.copy(
            url = url,
            isValidUrl = YouTubeUrlValidator.isValidYouTubeUrl(url)
        )
    }
    
    /**
     * Fetches available video qualities
     */
    fun fetchQualities() {
        val url = _uiState.value.url
        if (!YouTubeUrlValidator.isValidYouTubeUrl(url)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a valid YouTube URL"
            )
            return
        }
        
        viewModelScope.launch {
            downloadService.fetchVideoInfo(url).collect { state ->
                when (state) {
                    is DownloadState.FetchingInfo -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                    is DownloadState.InfoFetched -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            availableQualities = state.qualities,
                            errorMessage = null
                        )
                    }
                    is DownloadState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = state.message
                        )
                    }
                    else -> {}
                }
            }
        }
    }
    
    /**
     * Downloads the selected video quality
     */
    fun downloadVideo(quality: VideoQuality) {
        val url = _uiState.value.url
        if (!YouTubeUrlValidator.isValidYouTubeUrl(url)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a valid YouTube URL"
            )
            return
        }
        
        viewModelScope.launch {
            downloadService.downloadVideo(
                context = getApplication(),
                url = url,
                quality = quality
            ) { progress, speed, eta ->
                _uiState.value = _uiState.value.copy(
                    downloadProgress = progress,
                    downloadSpeed = speed,
                    estimatedTime = eta
                )
            }.collect { state ->
                _downloadState.value = state
                
                when (state) {
                    is DownloadState.Downloading -> {
                        _uiState.value = _uiState.value.copy(
                            isDownloading = true,
                            downloadProgress = state.progress,
                            downloadSpeed = state.speed,
                            estimatedTime = state.eta
                        )
                    }
                    is DownloadState.Completed -> {
                        _uiState.value = _uiState.value.copy(
                            isDownloading = false,
                            downloadProgress = 100,
                            successMessage = "Download completed: ${state.filePath}"
                        )
                    }
                    is DownloadState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isDownloading = false,
                            errorMessage = state.message
                        )
                    }
                    else -> {}
                }
            }
        }
    }
    
    /**
     * Clears all input and resets state
     */
    fun clearAll() {
        _uiState.value = YouTubeDownloadUiState()
        _downloadState.value = DownloadState.Idle
    }
    
    /**
     * Clears error messages
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}

/**
 * UI state for YouTube download screen
 */
data class YouTubeDownloadUiState(
    val url: String = "",
    val isValidUrl: Boolean = false,
    val isLoading: Boolean = false,
    val isDownloading: Boolean = false,
    val availableQualities: List<VideoQuality> = emptyList(),
    val selectedQuality: VideoQuality? = null,
    val downloadProgress: Int = 0,
    val downloadSpeed: String? = null,
    val estimatedTime: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
