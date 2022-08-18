package com.imashnake.animite.features.media

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.MediaRepository
import com.imashnake.animite.dev.extensions.toPrettyString
import com.imashnake.animite.features.home.HomeUiState
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MediaPageViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {
    var uiState by mutableStateOf(MediaUiState())
        private set

    private var fetchJob: Job? = null
    fun populateMediaPage(id: Int?, mediaType: MediaType) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val mediaRaw = mediaRepository.fetchMedia(id, mediaType).toPrettyString()

                uiState = with(uiState) {
                    copy(mediaRaw = mediaRaw)
                }
            } catch(ioe: IOException) {
                TODO()
            }
        }
    }
}
