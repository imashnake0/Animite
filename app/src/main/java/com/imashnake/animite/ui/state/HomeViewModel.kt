package com.imashnake.animite.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.data.repos.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    // TODO: Understand coroutines better.
    private var fetchJob: Job? = null

    fun addAnimes(vararg id: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val animeList = mutableListOf<AnimeQuery.Media?>()
                for (i in id) {
                    animeList.add(animeRepository.fetchAnime(i))
                }
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
