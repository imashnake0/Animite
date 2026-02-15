package com.imashnake.animite.media.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistSearchRepository
import com.imashnake.animite.api.anilist.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: AnilistSearchRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val query = savedStateHandle.getStateFlow(QUERY, MediaType.UNKNOWN__ to "")

    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean> = _loading

    @OptIn(FlowPreview::class)
    val searchList = query
        .onEach { _loading.update { true } }
        .debounce(300)
        .flatMapLatest { (mediaType, query) ->
            if (query.isEmpty()) {
                flowOf(Result.success(persistentListOf()))
            } else {
                searchRepository.fetchSearch(
                    type = mediaType,
                    perPage = 10,
                    search = query
                )
            }
        }
        .onEach { _loading.update { false } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Result.success(persistentListOf()))

    fun setQuery(mediaType: MediaType, query: String) {
        savedStateHandle[QUERY] = mediaType to query
    }

    companion object {
        const val QUERY = "query"
    }
}
