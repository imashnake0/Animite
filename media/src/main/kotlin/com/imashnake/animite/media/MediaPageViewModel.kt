package com.imashnake.animite.media

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: AnilistMediaRepository
) : ViewModel() {
    private val navArgs = savedStateHandle.toRoute<MediaPage>()
    private val mediaType = MediaType.safeValueOf(navArgs.mediaType)

    private val _uiState = MutableStateFlow<Resource<MediaUiState>>(Resource.loading())
    val uiState: StateFlow<Resource<MediaUiState>> = _uiState.asStateFlow()

    init {
        mediaRepository
            .fetchMedia(navArgs.id, mediaType)
            .onEach { result ->
                _uiState.value = result.fold(
                    onSuccess = { media ->
                        Resource.Success(
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
                        )
                    },
                    onFailure = { error ->
                        Resource.Error(error.message ?: "Unknown error")
                    }
                )
            }
            .launchIn(viewModelScope)
    }


    fun getGenreMediaMediums(genre: String?) {
        viewModelScope.launch {
            if (genre == null) {
                _uiState.update { current ->
                    when (current) {
                        is Resource.Success -> {
                            Resource.Success(
                                current.data.copy(
                                    genreTitleList = current.data.genreTitleList?.first.orEmpty() to persistentListOf()
                                )
                            )
                        }
                        else -> current
                    }
                }
                return@launch
            }

            when (val mediaResource = uiState.value) {
                is Resource.Success -> {
                    val mediaType = mediaResource.data.type?.let {
                        MediaType.safeValueOf(it)
                    } ?: MediaType.UNKNOWN__

                    val list = mediaRepository.fetchMediaMediumList(
                        mediaType = mediaType,
                        sort = listOf(MediaSort.SCORE_DESC),
                        genre = genre,
                    ).firstOrNull()?.getOrNull()

                    _uiState.update { current ->
                        when (current) {
                            is Resource.Success -> {
                                Resource.Success(
                                    current.data.copy(genreTitleList = list?.let { genre to it })
                                )
                            }
                            else -> current
                        }
                    }
                }
                is Resource.Loading -> {
                    // TODO: Add skeleton loader for loading state
                }
                is Resource.Error -> {
                    // TODO: Add error state handling
                }
            }
        }
    }
}
