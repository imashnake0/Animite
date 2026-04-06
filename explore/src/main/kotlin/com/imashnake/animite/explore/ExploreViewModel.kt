package com.imashnake.animite.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.core.ui.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModel @Inject constructor(
    private val mediaListRepository: AnilistMediaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            setAllGenres(mediaListRepository.fetchMediaGenres().toSet())
        }
    }

    val selectedSort = savedStateHandle.getStateFlow(Constants.SORT, Media.Sort.POPULARITY)
    val isDescending = savedStateHandle.getStateFlow(Constants.ORDER, true)
    val searchQuery = savedStateHandle.getStateFlow<String?>(SEARCH_QUERY, null)
    private val _allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_GENRES, null)
    val allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_GENRES, null)
    val includedGenres = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.INCLUDED_GENRES, emptySet())
    val excludedGenres = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.EXCLUDED_GENRES, emptySet())
    val selectedYear = savedStateHandle.getStateFlow<Int?>(Constants.YEAR, null)

    val mediaSort = selectedSort
        .combine(isDescending, ::Pair)
        .map { (sort, isDescending) -> Media.Sort.pollute(sort, isDescending) }

    @OptIn(FlowPreview::class)
    val exploreList = combine(
        flow = mediaSort,
        flow2 = searchQuery,
        flow3 = includedGenres,
        flow4 = excludedGenres,
        flow5 = selectedYear,
        transform = ::Pentuple,
    ).debounce { (sort, searchQuery, includedGenres, excludedGenres, year) ->
        if (
            sort == MediaSort.POPULARITY_DESC &&
            searchQuery == null &&
            includedGenres == emptySet<String>() &&
            excludedGenres == emptySet<String>() &&
            year == null
        ) 0L else 500L
    }.flatMapLatest { (sort, searchQuery, includedGenres, excludedGenres, year) ->
        mediaListRepository.fetchMediaMediumList(
            mediaType = MediaType.ANIME,
            sort = listOf(sort),
            search = searchQuery,
            includedGenres = includedGenres.toList().ifEmpty { null },
            excludedGenres = excludedGenres.toList().ifEmpty { null },
            year = year
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

    fun setSearchQuery(searchQuery: String?) {
        savedStateHandle[SEARCH_QUERY] = searchQuery
    }

    fun setAllGenres(allGenres: Set<String>?) {
        savedStateHandle[Constants._ALL_GENRES] = allGenres
        savedStateHandle[Constants.ALL_GENRES] = allGenres
    }

    fun includeMediaGenre(genre: String?) {
        savedStateHandle[Constants.INCLUDED_GENRES] = includedGenres.value.plus(genre)
        savedStateHandle[Constants.ALL_GENRES] = allGenres.value?.minus(genre)
    }

    fun excludeMediaGenre(genre: String?) {
        savedStateHandle[Constants.EXCLUDED_GENRES] = excludedGenres.value.plus(genre)
        savedStateHandle[Constants.INCLUDED_GENRES] = includedGenres.value.minus(genre)
    }

    fun clearMediaGenre(genre: String?) {
        savedStateHandle[Constants.ALL_GENRES] = allGenres.value?.plus(genre)
        savedStateHandle[Constants.EXCLUDED_GENRES] = excludedGenres.value.minus(genre)
    }

    fun setMediaYear(year: Int?) {
        savedStateHandle[Constants.YEAR] = year
    }

    fun resetGenres() {
        savedStateHandle[Constants.ALL_GENRES] = _allGenres.value
        savedStateHandle[Constants.INCLUDED_GENRES] = emptySet<String>()
        savedStateHandle[Constants.EXCLUDED_GENRES] = emptySet<String>()
    }

    fun reset() {
        savedStateHandle[Constants.SORT] = Media.Sort.POPULARITY
        savedStateHandle[Constants.ORDER] = true

        resetGenres()

        savedStateHandle[Constants.YEAR] = null
        savedStateHandle[SEARCH_QUERY] = null
    }

    val yearRange = getYears()

    private fun getYears(): ImmutableList<Int> {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val currentYear = now.toLocalDateTime(timeZone).year
        return (EARLIEST_YEAR..currentYear + 1).toImmutableList()
    }

    companion object {
        private const val SEARCH_QUERY = "searchQuery"
        private const val EARLIEST_YEAR = 1940
    }
}

// TODO: Move this to core?
data class Pentuple<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
) {
    /**
     * Returns string representation of the [Pentuple] including its
     * [first], [second], [third], [fourth], and [fifth] values.
     */
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth)"
}
