package com.imashnake.animite.features.media

import android.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.MediaQuery
import com.imashnake.animite.data.repos.MediaRepository
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.type.MediaRankType
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MediaPageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository
) : ViewModel() {
    private val navArgs = MediaPageDestination.argsFrom(savedStateHandle)

    var uiState by mutableStateOf(MediaUiState())
        private set

    init {
        viewModelScope.launch {
            try {
                val mediaType = MediaType.safeValueOf(navArgs.mediaType)
                val media = mediaRepository.fetchMedia(navArgs.id, mediaType)

                val score = media?.averageScore?.let { listOf(Stat(StatLabel.SCORE, it)) } ?: emptyList()

                val rankOptions = mapOf(
                    MediaRankType.RATED to StatLabel.RATING,
                    MediaRankType.POPULAR to StatLabel.POPULARITY,
                )
                // TODO: This code is a little sus, see if we can create internal models.
                val ranks = media?.rankings
                    ?.filter { it != null && it.type in rankOptions.keys && it.allTime == true }
                    ?.map { Stat(rankOptions[it!!.type] ?: StatLabel.UNKNOWN, it.rank) }
                    ?: emptyList()

                uiState = with(uiState) {
                    copy(
                        bannerImage = media?.bannerImage,
                        coverImage = media?.coverImage?.extraLarge,
                        color = media?.coverImage?.color?.let { Color.parseColor(it) },
                        title = media?.title?.romaji ?:
                        media?.title?.english ?:
                        media?.title?.native,
                        description = media?.description,
                        stats = score + ranks,
                        genres = media?.genres?.filterNotNull(),
                        characters = media?.characters?.nodes?.map {
                            Character(
                                id = it?.id,
                                image = it?.image?.large,
                                name = it?.name?.full
                            )
                        },
                        trailer = media?.trailer?.toUiModel()
                    )
                }
            } catch(ioe: IOException) {
                TODO()
            }
        }
    }

    private fun MediaQuery.Trailer.toUiModel(): Trailer? {
        // Give up if we don't have the data we want
        if (site == null || thumbnail == null || id == null) return null
        return Trailer(
            link = when (site) {
                "youtube" -> "https://www.youtube.com/watch?v=${id}"
                "dailymotion" -> "https://www.dailymotion.com/video/${id}"
                else -> error("This site type ($site) is not supported!")
            },
            thumbnail = when (site) {
                // TODO: Does a high resolution image always exist?
                "youtube" -> "https://img.youtube.com/vi/${id}/maxresdefault.jpg"
                // TODO: Change the icon and handle this properly.
                "dailymotion" -> thumbnail
                else -> error("This site type ($site) is not supported!")
            }
        )
    }
}
