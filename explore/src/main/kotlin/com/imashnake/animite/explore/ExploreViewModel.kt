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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModel @Inject constructor(
    mediaListRepository: AnilistMediaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val selectedSort = savedStateHandle.getStateFlow(Constants.SORT, Media.Sort.POPULARITY)
    val isDescending = savedStateHandle.getStateFlow(Constants.ORDER, true)
    val searchQuery = savedStateHandle.getStateFlow<String?>(SEARCH_QUERY, null)
    val selectedGenre = savedStateHandle.getStateFlow<String?>(Constants.GENRE, null)
    val selectedYear = savedStateHandle.getStateFlow<Int?>(Constants.YEAR, null)

    val mediaSort = selectedSort
        .combine(isDescending, ::Pair)
        .map { (sort, isDescending) -> Media.Sort.pollute(sort, isDescending) }

    val exploreList = combine(
        flow = mediaSort,
        flow2 = searchQuery,
        flow3 = selectedGenre,
        flow4 = selectedYear,
        transform = ::Quatruple,
    ).flatMapLatest { (sort, searchQuery, genre, year) ->
        mediaListRepository.fetchMediaMediumList(
            mediaType = MediaType.ANIME,
            sort = listOf(sort),
            search = searchQuery,
            genre = genre,
            year = year
        ).asResource()
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading()
    )

    val genres = mediaListRepository
        .fetchMediaGenres()
        .asResource()
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

    fun setSearchQuery(searchQuery: String?) {
        savedStateHandle[SEARCH_QUERY] = searchQuery
    }

    fun setMediaGenre(genre: String?) {
        savedStateHandle[Constants.GENRE] = genre
    }

    fun setMediaYear(year: Int?) {
        savedStateHandle[Constants.YEAR] = year
    }

    fun getCurrentYear(): Int {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val currentYear = now.toLocalDateTime(timeZone).year
        return currentYear
    }

    companion object {
        private const val SEARCH_QUERY = "searchQuery"
    }
}

data class Quatruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) {
    /**
     * Returns string representation of the [Quatruple] including its [first], [second], [third], and [fourth] values.
     */
    override fun toString(): String = "($first, $second, $third)"
}
