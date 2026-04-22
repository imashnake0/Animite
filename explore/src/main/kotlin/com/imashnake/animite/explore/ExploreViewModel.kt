package com.imashnake.animite.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaFormat
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaStatus
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.core.ui.Constants
import com.imashnake.animite.navigation.ExploreRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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
    private val navArgs = savedStateHandle.toRoute<ExploreRoute>()

    init {
        ChipFilterType.entries.forEach { filterType ->
            viewModelScope.launch(Dispatchers.IO) {
                setAllFilters(
                    filterType = filterType,
                    allFilters = when (filterType) {
                        ChipFilterType.GENRE -> mediaListRepository.fetchMediaGenres().toSet()
                        ChipFilterType.FORMAT -> Media.Format.animeFormats().map { it.name }.toSet()
                        ChipFilterType.STATUS -> Media.Status.entries.map { it.name }.toSet()
                    }
                )
            }
        }
    }

    val selectedSort = savedStateHandle.getStateFlow(Constants.SORT, navArgs.sortName?.let { Media.Sort.valueOf(it) } ?: Media.Sort.POPULARITY)
    val isDescending = savedStateHandle.getStateFlow(Constants.ORDER, navArgs.isDescending ?: true)

    val searchQuery = savedStateHandle.getStateFlow<String?>(SEARCH_QUERY, null)

    private val _allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_FILTERS + Constants.GENRES, null)
    private val allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_FILTERS + Constants.GENRES, null)
    private val includedGenres = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.INCLUDED_FILTERS + Constants.GENRES, emptySet())
    private val excludedGenres = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.EXCLUDED_FILTERS + Constants.GENRES, emptySet())

    val chipGenreGroup = combine(allGenres, includedGenres, excludedGenres) { (all, included, excluded) ->
        ChipFilterGroup(
            type = ChipFilterType.GENRE,
            allFilters = all.orEmpty().sorted().toImmutableList(),
            includedFilters = included.orEmpty().sorted().toImmutableList(),
            excludedFilters = excluded.orEmpty().sorted().toImmutableList()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChipFilterGroup(type = ChipFilterType.GENRE, persistentListOf(), persistentListOf(), persistentListOf())
    )

    val selectedSeason = savedStateHandle.getStateFlow(Constants.SEASON, navArgs.season)
    val selectedYear = savedStateHandle.getStateFlow(Constants.YEAR, navArgs.year)

    private val _allFormats = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_FILTERS + Constants.FORMATS, null)
    private val allFormats = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_FILTERS + Constants.FORMATS, null)
    private val includedFormats = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.INCLUDED_FILTERS + Constants.FORMATS, emptySet())
    private val excludedFormats = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.EXCLUDED_FILTERS + Constants.FORMATS, emptySet())

    val chipFormatGroup = combine(allFormats, includedFormats, excludedFormats) { (all, included, excluded) ->
        ChipFilterGroup(
            type = ChipFilterType.FORMAT,
            allFilters = all.orEmpty().sorted().toImmutableList(),
            includedFilters = included.orEmpty().sorted().toImmutableList(),
            excludedFilters = excluded.orEmpty().sorted().toImmutableList()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChipFilterGroup(type = ChipFilterType.GENRE, persistentListOf(), persistentListOf(), persistentListOf())
    )

    private val _allStatuses = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_FILTERS + Constants.STATUSES, null)
    private val allStatuses = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_FILTERS + Constants.STATUSES, null)
    private val includedStatuses = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.INCLUDED_FILTERS + Constants.STATUSES, emptySet())
    private val excludedStatuses = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.EXCLUDED_FILTERS + Constants.STATUSES, emptySet())

    val chipStatusGroup = combine(allStatuses, includedStatuses, excludedStatuses) { (all, included, excluded) ->
        ChipFilterGroup(
            type = ChipFilterType.STATUS,
            allFilters = all.orEmpty().sorted().toImmutableList(),
            includedFilters = included.orEmpty().sorted().toImmutableList(),
            excludedFilters = excluded.orEmpty().sorted().toImmutableList()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChipFilterGroup(type = ChipFilterType.GENRE, persistentListOf(), persistentListOf(), persistentListOf())
    )

    val mediaSort = selectedSort
        .combine(isDescending, ::Pair)
        .map { (sort, isDescending) -> Media.Sort.pollute(sort, isDescending) }

    @OptIn(FlowPreview::class)
    val exploreList = combine(
        flow = mediaSort,
        flow2 = searchQuery,
        flow3 = includedGenres,
        flow4 = excludedGenres,
        flow5 = selectedSeason.combine(selectedYear, ::Pair),
        flow6 = includedFormats,
        flow7 = excludedFormats,
        flow8 = includedStatuses,
        flow9 = excludedStatuses,
        transform = ::Nonuple,
    ).debounce { (sort, searchQuery, includedGenres, excludedGenres, seasonYear, includedFormats, excludedFormats) ->
        if (
            sort == MediaSort.POPULARITY_DESC &&
            searchQuery == null &&
            includedGenres == emptySet<String>() &&
            excludedGenres == emptySet<String>() &&
            seasonYear.first == null &&
            seasonYear.second == null &&
            includedFormats == emptySet<String>() &&
            excludedFormats == emptySet<String>()
        ) 0L else 500L
    }.flatMapLatest { (sort, searchQuery, includedGenres, excludedGenres, seasonYear, includedFormats, excludedFormats, includedStatuses, excludedStatuses) ->
        mediaListRepository.fetchMediaMediumList(
            mediaType = MediaType.ANIME,
            sort = listOf(sort),
            search = searchQuery,
            includedGenres = includedGenres.toList().ifEmpty { null },
            excludedGenres = excludedGenres.toList().ifEmpty { null },
            season = seasonYear.first?.let { MediaSeason.safeValueOf(it) },
            year = seasonYear.second,
            includedFormats = includedFormats.map { MediaFormat.valueOf(it) }.ifEmpty { null },
            excludedFormats = excludedFormats.map { MediaFormat.valueOf(it) }.ifEmpty { null },
            includedStatuses = includedStatuses.map { MediaStatus.valueOf(it) }.ifEmpty { null },
            excludedStatuses = excludedStatuses.map { MediaStatus.valueOf(it) }.ifEmpty { null },
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

    private fun setAllFilters(filterType: ChipFilterType, allFilters: Set<String>?) {
        savedStateHandle[Constants._ALL_FILTERS + filterType.tag] = allFilters
        savedStateHandle[Constants.ALL_FILTERS + filterType.tag] = allFilters
    }

    fun includeFilter(filterType: ChipFilterType, filter: String?) {
        savedStateHandle[Constants.INCLUDED_FILTERS + filterType.tag] = when(filterType) {
            ChipFilterType.GENRE -> includedGenres
            ChipFilterType.FORMAT -> includedFormats
            ChipFilterType.STATUS -> includedStatuses
        }.value.plus(filter)
        savedStateHandle[Constants.ALL_FILTERS + filterType.tag] = when(filterType) {
            ChipFilterType.GENRE -> allGenres
            ChipFilterType.FORMAT -> allFormats
            ChipFilterType.STATUS -> allStatuses
        }.value?.minus(filter)
    }

    fun excludeFilter(filterType: ChipFilterType, filter: String?) {
        savedStateHandle[Constants.EXCLUDED_FILTERS + filterType.tag] = when(filterType) {
            ChipFilterType.GENRE -> excludedGenres
            ChipFilterType.FORMAT -> excludedFormats
            ChipFilterType.STATUS -> excludedStatuses
        }.value.plus(filter)
        savedStateHandle[Constants.INCLUDED_FILTERS + filterType.tag] = when(filterType) {
            ChipFilterType.GENRE -> includedGenres
            ChipFilterType.FORMAT -> includedFormats
            ChipFilterType.STATUS -> includedStatuses
        }.value.minus(filter)
    }

    fun clearFilter(filterType: ChipFilterType, filter: String?) {
        savedStateHandle[Constants.ALL_FILTERS + filterType.tag] = when(filterType) {
            ChipFilterType.GENRE -> allGenres
            ChipFilterType.FORMAT -> allFormats
            ChipFilterType.STATUS -> allStatuses
        }.value?.plus(filter)
        savedStateHandle[Constants.EXCLUDED_FILTERS + filterType.tag] = when(filterType) {
            ChipFilterType.GENRE -> excludedGenres
            ChipFilterType.FORMAT -> excludedFormats
            ChipFilterType.STATUS -> excludedStatuses
        }.value.minus(filter)
    }

    fun setMediaSeason(season: String?) {
        savedStateHandle[Constants.SEASON] = season
    }

    fun setMediaYear(year: Int?) {
        savedStateHandle[Constants.YEAR] = year
    }

    fun resetFilter(filterType: ChipFilterType) {
        savedStateHandle[Constants.ALL_FILTERS + filterType.tag] = when(filterType) {
            ChipFilterType.GENRE -> _allGenres
            ChipFilterType.FORMAT -> _allFormats
            ChipFilterType.STATUS -> _allStatuses
        }.value
        savedStateHandle[Constants.INCLUDED_FILTERS + filterType.tag] = emptySet<String>()
        savedStateHandle[Constants.EXCLUDED_FILTERS + filterType.tag] = emptySet<String>()
    }

    fun reset() {
        savedStateHandle[SEARCH_QUERY] = null

        savedStateHandle[Constants.SORT] = Media.Sort.POPULARITY
        savedStateHandle[Constants.ORDER] = true

        savedStateHandle[Constants.SEASON] = null
        savedStateHandle[Constants.YEAR] = null

        ChipFilterType.entries.forEach {
            resetFilter(it)
        }
    }

    val yearRange = getYears()

    private fun getYears(): IntRange {
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val currentYear = now.toLocalDateTime(timeZone).year
        return EARLIEST_YEAR..currentYear + 1
    }

    companion object {
        private const val SEARCH_QUERY = "searchQuery"
        private const val EARLIEST_YEAR = 1940
    }

    enum class ChipFilterType(val tag: String) {
        GENRE(Constants.GENRES),
        FORMAT(Constants.FORMATS),
        STATUS(Constants.STATUSES)
    }

    data class ChipFilterGroup(
        val type: ChipFilterType,
        val allFilters: ImmutableList<String>,
        val includedFilters: ImmutableList<String>,
        val excludedFilters: ImmutableList<String>,
    )
}

// TODO: Move this to core?
data class Nonuple<out A, out B, out C, out D, out E, out F, out G, out H, out I>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G,
    val eighth: H,
    val ninth: I,
) {
    /**
     * Returns string representation of the [Nonuple] including its
     * [first], [second], [third], [fourth], [fifth], [sixth], [seventh], [eighth], and [ninth] values.
     */
    override fun toString(): String =
        "($first, $second, $third, $fourth, $fifth, $sixth, $seventh, $eighth, $ninth)"
}

inline fun <
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
//        T10,
        R
> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
//    flow10: Flow<T10>,
    crossinline transform: suspend (
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
//        T10
    ) -> R,
): Flow<R> = combine(
    flow,
    flow2,
    flow3,
    flow4,
    flow5,
    flow6,
    flow7,
    flow8,
    flow9,
//    flow10
) { args: Array<*> ->
    @Suppress("UNCHECKED_CAST")
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
        args[6] as T7,
        args[7] as T8,
        args[8] as T9,
//        args[9] as T10,
    )
}
