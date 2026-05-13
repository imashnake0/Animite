package com.imashnake.animite.explore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.settings.Prefs
import com.imashnake.animite.api.anilist.type.MediaFormat
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaStatus
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    private val savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
) : ViewModel() {
    private val navArgs = savedStateHandle.toRoute<ExploreRoute>()

    val selectedSort = savedStateHandle.getStateFlow(Constants.SORT, navArgs.sortName?.let { Media.Sort.valueOf(it) } ?: Media.Sort.POPULARITY)
    val isDescending = savedStateHandle.getStateFlow(Constants.ORDER, navArgs.isDescending ?: true)

    val searchQuery = savedStateHandle.getStateFlow<String?>(SEARCH_QUERY, null)

    val mediaType = savedStateHandle.getStateFlow(Constants.MEDIA_TYPE_FILTER, navArgs.mediaType ?: MediaType.ANIME.name)

    private val _allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_FILTERS + Constants.GENRES, null)
    private val allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_FILTERS + Constants.GENRES, null)
    private val includedGenres = savedStateHandle.getMutableStateFlow(Constants.INCLUDED_FILTERS + Constants.GENRES, setOfNotNull(navArgs.genre))
    private val excludedGenres = savedStateHandle.getMutableStateFlow(Constants.EXCLUDED_FILTERS + Constants.GENRES, emptySet<String>())
    private val genreSegregation = combine(
        flow = includedGenres,
        flow2 = excludedGenres,
        transform = ::Pair
    ).map { ChipFilterSegregation(it.first, it.second) }

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
    val seasonYear = selectedSeason.combine(selectedYear) { season, year ->
        SeasonYear(season, year)
    }

    private val _allFormats = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_FILTERS + Constants.FORMATS, null)
    private val allFormats = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_FILTERS + Constants.FORMATS, null)
    private val includedFormats = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.INCLUDED_FILTERS + Constants.FORMATS, emptySet())
    private val excludedFormats = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.EXCLUDED_FILTERS + Constants.FORMATS, emptySet())
    private val formatSegregation = combine(
        flow = includedFormats,
        flow2 = excludedFormats,
        transform = ::Pair
    ).map { ChipFilterSegregation(it.first, it.second) }

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
    private val statusSegregation = combine(
        flow = includedStatuses,
        flow2 = excludedStatuses,
        transform = ::Pair
    ).map { ChipFilterSegregation(it.first, it.second) }

    private val page = savedStateHandle.getMutableStateFlow(Constants.PAGE, 1)

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

    val isAdult = savedStateHandle.getStateFlow<Boolean?>(Constants.IS_ADULT, false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            ChipFilterType.entries.forEach { filterType ->
                setAllFilters(
                    filterType = filterType,
                    allFilters = when (filterType) {
                        ChipFilterType.GENRE -> mediaListRepository.fetchMediaGenres(
                            preferencesRepository.isNsfwEnabled.filterNotNull().first()
                        ).toSet()
                        ChipFilterType.FORMAT -> Media.Format.animeFormats().map { it.name }.toSet()
                        ChipFilterType.STATUS -> Media.Status.entries.map { it.name }.toSet()
                    }
                )
            }
            navArgs.genre?.let {
                includeFilter(
                    filterType = ChipFilterType.GENRE,
                    filter = it
                )
            }
        }
    }

    val filterSheetOptions = combine(
        flow = genreSegregation,
        flow2 = seasonYear,
        flow3 = formatSegregation,
        flow4 = statusSegregation,
        flow5 = isAdult.filterNotNull(),
        flow6 = mediaType.onEach {
            val mediaType = MediaType.valueOf(it)
            if (mediaType == MediaType.MANGA) {
                setMediaSeason(null)
            }
            setAllFilters(
                filterType = ChipFilterType.FORMAT,
                allFilters = when (MediaType.valueOf(it)) {
                    MediaType.ANIME -> Media.Format.animeFormats()
                    else -> Media.Format.mangaFormats()
                }.map { format -> format.name }.toSet()
            )
            resetFilter(ChipFilterType.FORMAT)
        },
        transform = { genres, seasonYear, format, status, isAdult, type ->
            FilterSheetOptions(
                genres = genres,
                seasonYear = seasonYear,
                format = format,
                status = status,
                isAdult = isAdult,
                mediaType = type,
            )
        }
    )

    val isNsfwEnabled = preferencesRepository.isNsfwEnabled.filterNotNull()

    val prefs = combine(
        flow = preferencesRepository.isNsfwEnabled.filterNotNull(),
        flow2 = preferencesRepository.language.filterNotNull(),
        transform = { isNsfwEnabled, language ->
            Prefs(isNsfwEnabled, Media.Language.valueOf(language))
        }
    )

    var shouldDebounce = false

    @OptIn(FlowPreview::class)
    val explorePage = combine(
        flow = mediaSort,
        flow2 = searchQuery,
        flow3 = filterSheetOptions,
        flow4 = page,
        flow5 = prefs,
        transform = ::Pentuple,
    ).debounce {
        if (shouldDebounce) {
            500L
        } else {
            shouldDebounce = true
            0L
        }
    }.flatMapLatest { (sort, searchQuery, filterSheetOptions, page, prefs) ->
        flow {
            emit(Resource.loading())
            emit(
                mediaListRepository.fetchMediaMediumList(
                    mediaType = MediaType.valueOf(filterSheetOptions.mediaType),
                    sort = listOf(sort),
                    page = page,
                    search = searchQuery,
                    includedGenres = filterSheetOptions.genres.included.toList().ifEmpty { null },
                    excludedGenres = filterSheetOptions.genres.excluded.toList().ifEmpty { null },
                    season = filterSheetOptions.seasonYear.season?.let { MediaSeason.safeValueOf(it) },
                    year = filterSheetOptions.seasonYear.year,
                    includedFormats = filterSheetOptions.format.included.map { MediaFormat.valueOf(it) }.ifEmpty { null },
                    excludedFormats = filterSheetOptions.format.excluded.map { MediaFormat.valueOf(it) }.ifEmpty { null },
                    includedStatuses = filterSheetOptions.status.included.map { MediaStatus.valueOf(it) }.ifEmpty { null },
                    excludedStatuses = filterSheetOptions.status.excluded.map { MediaStatus.valueOf(it) }.ifEmpty { null },
                    isAdult = filterSheetOptions.isAdult,
                    isNsfwEnabled = prefs.isNsfwEnabled,
                    language = prefs.language
                ).asResource().first()
            )
        }
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

    fun setMediaType(type: String) {
        savedStateHandle[Constants.MEDIA_TYPE_FILTER] = type
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

    fun setPage(page: Int) {
        savedStateHandle[Constants.PAGE] = page
    }

    fun setIsAdult(isAdult: Boolean) {
        savedStateHandle[Constants.IS_ADULT] = isAdult
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

        savedStateHandle[Constants.IS_ADULT] = false

        ChipFilterType.entries.forEach {
            resetFilter(it)
        }

        savedStateHandle[Constants.PAGE] = 1
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

    data class ChipFilterSegregation(
        val included: Set<String>,
        val excluded: Set<String>,
    )

    data class SeasonYear(
        val season: String?,
        val year: Int?,
    )

    data class FilterSheetOptions(
        val genres: ChipFilterSegregation,
        val seasonYear: SeasonYear,
        val format: ChipFilterSegregation,
        val status: ChipFilterSegregation,
        val isAdult: Boolean,
        val mediaType: String,
    )
}

// TODO: Move this to core?
data class Pentuple<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
) {
    /**
     * Returns string representation of the [Pentuple] including its
     * [first], [second], [third], [fourth], and [fifth]
     */
    override fun toString(): String =
        "($first, $second, $third, $fourth, $fifth)"
}

inline fun <
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        R
        > combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    crossinline transform: suspend (
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
    ) -> R,
): Flow<R> = combine(
    flow,
    flow2,
    flow3,
    flow4,
    flow5,
    flow6,
) { args: Array<*> ->
    @Suppress("UNCHECKED_CAST")
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
    )
}
