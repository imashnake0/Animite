package com.imashnake.animite.media

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@Suppress("SwallowedException")
class MediaPageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: AnilistMediaRepository
) : ViewModel() {
    private val navArgs = savedStateHandle.toRoute<MediaPage>()
    var uiState by mutableStateOf(MediaUiState(
            source = navArgs.source,
            id = navArgs.id,
            type = navArgs.mediaType,
            title = navArgs.title
        ))
        private set

    init {
        viewModelScope.launch {
            try {
                val mediaType = MediaType.safeValueOf(navArgs.mediaType)
                // TODO: Switch to StateFlows.
                val media = mediaRepository
                    .fetchMedia(navArgs.id, mediaType)
                    .firstOrNull()
                    ?.getOrNull()

                uiState = uiState.copy(
                    bannerImage = media?.bannerImage,
                    coverImage = media?.coverImage,
                    color = media?.color,
                    description = media?.description,
                    dayHoursToNextEpisode = media?.timeToEpisode?.first,
                    nextEpisode = media?.timeToEpisode?.second,
                    info = media?.info,
                    ranks = media?.rankings,
                    genres = media?.genres,
                    characters = media?.characters,
                    staff = media?.staff,
                    trailer = media?.trailer,
                    streamingEpisodes = media?.streamingEpisodes,
                    relations = media?.relations,
                    recommendations = media?.recommendations
                )
            } catch(_: IOException) {
                TODO()
            }
        }
    }

    fun getGenreMediaMediums(genre: String?) = viewModelScope.launch {
        if (genre == null) {
            uiState = uiState.copy(genreTitleList = uiState.genreTitleList?.first.orEmpty() to persistentListOf())
            return@launch
        }
        val list = mediaRepository.fetchMediaMediumList(
            mediaType = uiState.type?.let {
                MediaType.safeValueOf(it)
            } ?: MediaType.UNKNOWN__,
            sort = listOf(MediaSort.SCORE_DESC),
            genre = genre,
        ).firstOrNull()?.getOrNull()

        uiState = uiState.copy(genreTitleList = list?.let { genre to it })
    }
}
