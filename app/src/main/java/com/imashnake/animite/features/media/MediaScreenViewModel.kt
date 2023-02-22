package com.imashnake.animite.features.media

import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.MediaQuery
import com.imashnake.animite.api.anilist.type.MediaRankType
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.Resource.Companion.asResource
import com.imashnake.animite.features.destinations.MediaScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MediaScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    mediaRepository: AnilistMediaRepository
) : ViewModel() {
    private val navArgs = MediaScreenDestination.argsFrom(savedStateHandle)

    val media: StateFlow<Resource<Media>> = mediaRepository.fetchMedia(navArgs.id, MediaType.safeValueOf(navArgs.mediaType))
        .asResource { media ->
            val score = media.averageScore?.let { listOf(Stat(StatLabel.SCORE, it)) }
            val rankOptions = mapOf(
                MediaRankType.RATED to StatLabel.RATING,
                MediaRankType.POPULAR to StatLabel.POPULARITY,
            )
            // TODO: This code is a little sus, see if we can create internal models.
            val ranks = media.rankings
                ?.filter { it != null && it.type in rankOptions.keys && it.allTime == true }
                ?.map { Stat(rankOptions[it!!.type] ?: StatLabel.UNKNOWN, it.rank) }
            Media(
                bannerImage = media.bannerImage,
                coverImage = media.coverImage?.extraLarge,
                title = media.title?.getLocalizedTitle(),
                description = media.description,
                stats = score + ranks,
                genres = media.genres?.filterNotNull(),
                characters = media.characters?.nodes
                    ?.filterNotNull()
                    ?.map { it.toCharacter() }
                    .orEmpty(),
                trailer = media.trailer?.toUiModel(),
                baseColor = media.coverImage?.color?.parseColor()
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            Resource.loading()
        )

    private fun String.parseColor(): Int = Color.parseColor(this)

    private fun MediaQuery.Trailer.toUiModel(): Trailer? {
        // Give up if we don't have the data we want
        if (site == null || thumbnail == null || id == null) return null
        // TODO This could be an enum, or a sealed class to better capture data types
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
                "dailymotion" -> thumbnail!!
                else -> error("This site type ($site) is not supported!")
            }
        )
    }

    private fun MediaQuery.Title.getLocalizedTitle(): String {
        return this.romaji ?: this.english ?: this.native ?: error("No title found!")
    }
    private fun MediaQuery.Node.toCharacter(): Character {
        return Character(
            id = id,
            image = image?.large,
            name = name?.full
        )
    }
}
