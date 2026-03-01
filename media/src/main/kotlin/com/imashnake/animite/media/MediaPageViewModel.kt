package com.imashnake.animite.media

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: AnilistMediaRepository
) : ViewModel() {
    private val navArgs = savedStateHandle.toRoute<MediaPage>()
    private val _genreTitleListState = MutableStateFlow<MediaUiState?>(null)
    val genreTitleList: StateFlow<MediaUiState?> = _genreTitleListState

    private val mediaType = MediaType.safeValueOf(navArgs.mediaType)

    val uiState: StateFlow<Resource<MediaUiState>> = mediaRepository
        .fetchMedia(navArgs.id, mediaType)
        .asResource { media ->
            MediaUiState(
                source = navArgs.source,
                id = navArgs.id,
                type = navArgs.mediaType,
                title = navArgs.title,
                bannerImage = media.bannerImage,
                coverImage = media.coverImage,
                color = media.color,
                description = media.description,
                dayHoursToNextEpisode = media.timeToEpisode?.first,
                nextEpisode = media.timeToEpisode?.second,
                info = media.info,
                year = media.year,
                season = media.season,
                rankings = media.rankings,
                genres = media.genres,
                characters = media.characters,
                staff = media.staff,
                trailer = media.trailer,
                streamingEpisodes = media.streamingEpisodes,
                relations = media.relations,
                recommendations = media.recommendations
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.loading()
        )


    fun getGenreMediaMediums(genre: String?) = viewModelScope.launch {
        if (genre == null) {
            _genreTitleListState.value = _genreTitleListState.value?.copy(
                genreTitleList = _genreTitleListState.value?.genreTitleList?.first.orEmpty() to persistentListOf()
            )
            return@launch
        }
        val mediaType = uiState.value.data?.type?.let {
            MediaType.safeValueOf(it)
        } ?: MediaType.UNKNOWN__

        val list = mediaRepository.fetchMediaMediumList(
            mediaType = mediaType,
            sort = listOf(MediaSort.SCORE_DESC),
            genre = genre,
        ).firstOrNull()?.getOrNull()

        _genreTitleListState.update { current ->
            (current ?: uiState.value.data)?.copy(genreTitleList = list?.let { genre to it })
        }
    }
}
