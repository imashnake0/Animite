package com.imashnake.animite.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.SearchRepository
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

// TODO: Not sure if we need this class.
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
    var uiState by mutableStateOf(SearchUiState())
        private set

    // TODO: Understand coroutines better.
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
                    copy(
                        searchList = searchList
                    )
                }
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }
}
