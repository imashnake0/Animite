package com.imashnake.animite.media

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@Suppress("SwallowedException")
class MediaPageViewModel @Inject constructor(
    private val mediaRepository: AnilistMediaRepository,
    preferencesRepository: PreferencesRepository,
    savedStateHandle: SavedStateHandle,
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
                    .fetchMedia(
                        id = navArgs.id,
                        mediaType = mediaType,
                        language = preferencesRepository.language
                            .filterNotNull()
                            .mapNotNull { Media.Language.valueOf(it) }
                            .first()
                    )
                    .firstOrNull()
                    ?.getOrNull()

                uiState = uiState.copy(
                    bannerImage = media?.bannerImage,
                    coverImage = media?.coverImage,
                    color = media?.color,
                    description = media?.description,
                    nextAiring = media?.nextAiring,
                    info = media?.info,
                    year = media?.year,
                    season = media?.season,
                    rankings = media?.rankings,
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
}
