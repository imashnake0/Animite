package com.imashnake.animite.features.searchbar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistSearchRepository
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.core.ui.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: AnilistSearchRepository,
    private val savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository
): ViewModel() {
    private val mediaType = savedStateHandle.getStateFlow<MediaType?>(Constants.MEDIA_TYPE, null)
    private val query = savedStateHandle.getStateFlow<String?>(QUERY, null)

    @OptIn(FlowPreview::class)
    val searchList = combine(
        flow = mediaType.filterNotNull(),
        flow2 = query,
        flow3 = preferencesRepository.isNsfwEnabled.filterNotNull(),
        transform = ::Triple
    ).debounce(300L).flatMapLatest { (mediaType, query, isNsfwEnabled) ->
        if (query.isNullOrEmpty()) {
            flowOf(Resource.success(persistentListOf()))
        } else {
            searchRepository.fetchSearch(
                type = mediaType,
                perPage = 20,
                search = query,
                isNsfwEnabled = isNsfwEnabled,
            ).asResource()
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading()
    )

    fun setMediaType(mediaType: MediaType) {
        savedStateHandle[Constants.MEDIA_TYPE] = mediaType
    }

    fun setQuery(query: String?) {
        savedStateHandle[QUERY] = query
    }

    companion object {
        const val QUERY = "query"
    }
}
