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
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.core.ui.Constants
import com.imashnake.animite.navigation.ExploreRoute
import dagger.hilt.android.lifecycle.HiltViewModel
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
        setAllFormats(Media.Format.animeFormats().map { it.name }.toSet())
        viewModelScope.launch(Dispatchers.IO) {
            setAllGenres(mediaListRepository.fetchMediaGenres().toSet())
        }
    }

    val selectedSort = savedStateHandle.getStateFlow(Constants.SORT, navArgs.sortName?.let { Media.Sort.valueOf(it) } ?: Media.Sort.POPULARITY)
    val isDescending = savedStateHandle.getStateFlow(Constants.ORDER, navArgs.isDescending ?: true)

    val searchQuery = savedStateHandle.getStateFlow<String?>(SEARCH_QUERY, null)

    private val _allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_GENRES, null)
    val allGenres = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_GENRES, null)
    val includedGenres = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.INCLUDED_GENRES, emptySet())
    val excludedGenres = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.EXCLUDED_GENRES, emptySet())

    val selectedSeason = savedStateHandle.getStateFlow(Constants.SEASON, navArgs.season)
    val selectedYear = savedStateHandle.getStateFlow(Constants.YEAR, navArgs.year)

    private val _allFormats = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants._ALL_FORMATS, null)
    val allFormats = savedStateHandle.getMutableStateFlow<Set<String>?>(Constants.ALL_FORMATS, null)
    val includedFormats = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.INCLUDED_FORMATS, emptySet())
    val excludedFormats = savedStateHandle.getMutableStateFlow<Set<String>>(Constants.EXCLUDED_FORMATS, emptySet())

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
        transform = ::Septuple,
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
    }.flatMapLatest { (sort, searchQuery, includedGenres, excludedGenres, seasonYear, includedFormats, excludedFormats) ->
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

    fun setAllFormats(allFormats: Set<String>?) {
        savedStateHandle[Constants._ALL_FORMATS] = allFormats
        savedStateHandle[Constants.ALL_FORMATS] = allFormats
    }

    fun includeMediaFormat(format: String?) {
        savedStateHandle[Constants.INCLUDED_FORMATS] = includedFormats.value.plus(format)
        savedStateHandle[Constants.ALL_FORMATS] = allFormats.value?.minus(format)
    }

    fun excludeMediaFormat(format: String?) {
        savedStateHandle[Constants.EXCLUDED_FORMATS] = excludedFormats.value.plus(format)
        savedStateHandle[Constants.INCLUDED_FORMATS] = includedFormats.value.minus(format)
    }

    fun clearMediaFormat(format: String?) {
        savedStateHandle[Constants.ALL_FORMATS] = allFormats.value?.plus(format)
        savedStateHandle[Constants.EXCLUDED_FORMATS] = excludedFormats.value.minus(format)
    }

    fun setMediaSeason(season: String?) {
        savedStateHandle[Constants.SEASON] = season
    }

    fun setMediaYear(year: Int?) {
        savedStateHandle[Constants.YEAR] = year
    }

    fun resetGenres() {
        savedStateHandle[Constants.ALL_GENRES] = _allGenres.value
        savedStateHandle[Constants.INCLUDED_GENRES] = emptySet<String>()
        savedStateHandle[Constants.EXCLUDED_GENRES] = emptySet<String>()
    }

    fun resetFormats() {
        savedStateHandle[Constants.ALL_FORMATS] = _allFormats.value
        savedStateHandle[Constants.INCLUDED_FORMATS] = emptySet<String>()
        savedStateHandle[Constants.EXCLUDED_FORMATS] = emptySet<String>()
    }

    fun reset() {
        savedStateHandle[SEARCH_QUERY] = null

        savedStateHandle[Constants.SORT] = Media.Sort.POPULARITY
        savedStateHandle[Constants.ORDER] = true

        resetGenres()

        savedStateHandle[Constants.SEASON] = null
        savedStateHandle[Constants.YEAR] = null

        resetFormats()
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
}

// TODO: Move this to core?
data class Septuple<out A, out B, out C, out D, out E, out F, out G>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G,
) {
    /**
     * Returns string representation of the [Septuple] including its
     * [first], [second], [third], [fourth], [fifth], [sixth], and [seventh] values.
     */
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth, $sixth, $seventh)"
}

inline fun <
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
//        T8,
//        T9,
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
//    flow8: Flow<T8>,
//    flow9: Flow<T9>,
//    flow10: Flow<T10>,
    crossinline transform: suspend (
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
//        T8,
//        T9,
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
//    flow8,
//    flow9,
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
//        args[7] as T8,
//        args[8] as T9,
//        args[9] as T10,
    )
}
