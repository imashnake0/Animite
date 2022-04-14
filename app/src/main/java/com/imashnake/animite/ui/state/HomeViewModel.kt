package com.imashnake.animite.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.AnimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * TODO: Kdoc.
 */
class HomeViewModel(
    var animeRepository: AnimeRepository
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    // TODO: Understand coroutines better.
    private var fetchJob: Job? = null

    fun fetchAnime(id: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                // Only adding a single anime for the sake of simplicity.
                val anime = animeRepository.fetchAnime(id)
                uiState = uiState.copy(animeList = listOf(anime))
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }
}
