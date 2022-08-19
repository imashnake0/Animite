package com.imashnake.animite.features.media

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.MediaRepository
import com.imashnake.animite.type.MediaRankType
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
                val media = mediaRepository.fetchMedia(id, mediaType)

                val ranks = mutableListOf<Pair<String, Int>>()
                media?.rankings?.forEach {
                    if (it?.allTime == true) {
                        if (it.type == MediaRankType.RATED) ranks.add(Pair("RATING", it.rank))
                        if (it.type == MediaRankType.POPULAR) ranks.add(Pair("POPULARITY", it.rank))
                    }
                }

                uiState = with(uiState) {
                    copy(
                        bannerImage = media?.bannerImage,
                        coverImage = media?.coverImage?.extraLarge,
                        title = media?.title?.romaji ?:
                                media?.title?.english ?:
                                media?.title?.native,
                        description = media?.description,
                        averageScore = media?.averageScore,
                        ranks = ranks,
                        genres = media?.genres,
                        characters = media?.characters?.nodes?.map {
                                it -> Pair(it?.image?.large, it?.name?.full)
                        },
                        trailer = Pair(
                            first = if (media?.trailer?.site == "youtube") {
                                "https://www.youtube.com/watch?v=${media.trailer.id}"
                            } else {
                                "https://www.dailymotion.com/video/${media?.trailer?.id}"
                            },
                            second = media?.trailer?.thumbnail
                        )
                    )
                }
            } catch(ioe: IOException) {
                TODO()
            }
        }
    }
}
