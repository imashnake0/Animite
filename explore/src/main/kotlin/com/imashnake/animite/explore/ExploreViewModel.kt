package com.imashnake.animite.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.core.ui.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModel @Inject constructor(
    mediaListRepository: AnilistMediaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val selectedSort = savedStateHandle.getStateFlow(Constants.SORT, Media.Sort.POPULARITY)
    val isDescending = savedStateHandle.getStateFlow(Constants.ORDER, true)

    val exploreList = selectedSort
        .combine(isDescending, ::Pair)
        .map { (sort, isDescending) -> Media.Sort.pollute(sort, isDescending) }
        .flatMapLatest { sort ->
            mediaListRepository.fetchMediaMediumList(
                mediaType = MediaType.ANIME,
                sort = listOf(sort)
            ).asResource()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = Resource.loading()
        )

    fun setMediaSort(sort: Media.Sort) {
        savedStateHandle[Constants.SORT] = sort
    }

    fun setIsDescending(isDescending: Boolean) {
        savedStateHandle[Constants.ORDER] = isDescending
    }
}
