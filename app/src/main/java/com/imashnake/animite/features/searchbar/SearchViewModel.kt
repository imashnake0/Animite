package com.imashnake.animite.features.searchbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.SearchRepository
import com.imashnake.animite.features.searchbar.SearchUiState
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
    var uiState by mutableStateOf(SearchUiState())
        private set

    private var fetchJob: Job? = null

    fun searchAnime(search: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val searchList = searchRepository.search(
                    mediaType = MediaType.ANIME,
                    perPage = 10,
                    search = search
                )

                uiState = with(uiState) {
                    copy(searchList = searchList)
                }
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }

    fun clearList() {
        uiState = with(uiState) {
            copy(searchList = null)
        }
    }
}
