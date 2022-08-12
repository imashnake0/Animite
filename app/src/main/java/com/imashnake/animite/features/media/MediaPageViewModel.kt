package com.imashnake.animite.features.media

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.MediaRepository
import com.imashnake.animite.dev.extensions.toPrettyString
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPageViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {
    // TODO: Understand coroutines better.
    private var fetchJob: Job? = null

    // TODO: Actually navigate to media.
    fun fetchMedia(id: Int?, mediaType: MediaType) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val clickedMedia = mediaRepository.fetchMedia(id, mediaType)

            Log.d(
                "ClickedMedia",
                clickedMedia.toPrettyString()
            )
        }
    }
}
