package com.imashnake.animite.features.searchbar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.repos.SearchRepository
import com.imashnake.animite.dev.ext.string
import com.imashnake.animite.dev.internal.Constants
import com.imashnake.animite.type.MediaFormat
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val mediaType = savedStateHandle.getStateFlow<MediaType?>(Constants.MEDIA_TYPE, null)
    private val query = savedStateHandle.getStateFlow<String?>(QUERY, null)

    fun setMediaType(mediaType: MediaType) {
        savedStateHandle[Constants.MEDIA_TYPE] = mediaType
    }

    fun setQuery(query: String?) {
        savedStateHandle[QUERY] = query
    }

    val searchList = mediaType
        .filterNotNull()
        .combine(query, ::Pair)
        .flatMapLatest { (mediaType, query) ->
            if (query.isNullOrEmpty()) {
                flowOf(Resource.success(SearchQuery.Page(emptyList())))
            } else {
                searchRepository.search(
                    mediaType = mediaType,
                    search = query
                )
            }
        }
        .map { resource ->
            if (resource is Resource.Success) {
                Resource.success(
                    resource.data?.media?.mapNotNull {
                        if (it != null) {
                            SearchItem(
                                id = it.id,
                                image = it.coverImage?.extraLarge,
                                title = it.title?.romaji ?: it.title?.english ?: it.title?.romaji,
                                seasonYear = listOfNotNull(it.season?.string, it.seasonYear).joinToString(separator = " "),
                                studios = it.studios?.nodes?.mapNotNull { studio -> studio?.name }?.joinToString(separator = ", "),
                                footer = listOfNotNull(
                                    it.format?.takeIf { format -> format != MediaFormat.UNKNOWN__ }?.rawValue?.replace("_", " "),
                                    it.episodes?.let { ep -> "$ep ${if (ep == 1) "episode" else "episodes"}"}
                                ).joinToString(separator = " ꞏ ")
                            )
                        } else null
                    }
                )
            } else {
                Resource.error("")
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    companion object {
        const val QUERY = "query"
    }
}
