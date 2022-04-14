package com.imashnake.animite.ui.state

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.data.repos.AnimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * TODO: Kdoc.
 */
class HomeViewModel(
    private val animeRepository: AnimeRepository
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    // TODO: Understand coroutines better.
    private var fetchJob: Job? = null

    fun addAnime(id: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                // Only adding a single anime for the sake of simplicity.
                val animeList = mutableListOf<AnimeQuery.Media?>()
                animeList.add(animeRepository.fetchAnime(id))
                uiState = uiState.copy(animeList = animeList)
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }
}

/**
 * TODO: Kdoc.
 */
class HomeViewModelFactory(private val animeRepository: AnimeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(animeRepository) as T
    }
}
