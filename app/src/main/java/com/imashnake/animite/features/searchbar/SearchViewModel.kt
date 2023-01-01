package com.imashnake.animite.features.searchbar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.repos.SearchRepository
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val mediaType = savedStateHandle.getStateFlow<MediaType?>("mediaType", null)
    private val query = savedStateHandle.getStateFlow<String?>("query", null)

    fun setMediaType(mediaType: MediaType) {
        savedStateHandle["mediaType"] = mediaType
    }

    fun setQuery(query: String?) {
        savedStateHandle["query"] = query
    }

    val searchList = mediaType
        .filterNotNull()
        .combine(query, ::Pair)
        .flatMapLatest { (mediaType, query) ->
            searchRepository.search(
                mediaType = mediaType,
                search = query
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())
}
