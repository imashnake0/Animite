package com.imashnake.animite.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.model.AnimeList
import com.imashnake.animite.core.model.MangaList
import com.imashnake.animite.core.ui.Density
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    val theme = preferencesRepository.theme.filterNotNull()
    val useSystemColorScheme = preferencesRepository.useSystemColorScheme.filterNotNull()
    val isAmoled = preferencesRepository.isAmoled.filterNotNull()
    val density = preferencesRepository.density.filterNotNull()
    val isNsfwEnabled = preferencesRepository.isNsfwEnabled.filterNotNull()
    val language = preferencesRepository.language.filterNotNull()
    val listSize = preferencesRepository.listSize.filterNotNull()
    val isDevOptionsEnabled = preferencesRepository.isDevOptionsEnabled.filterNotNull()
    val dayHour = preferencesRepository.dayHour
    val animeList = preferencesRepository.animeListsIndices.map { indices ->
        indices?.map {
            // each int from +-1 to +-5 maps to a AnimeList
            when (it.toInt()) {
                1, -1 -> AnimeList.TRENDING_NOW
                2, -2 -> AnimeList.POPULAR_THIS_SEASON
                3, -3 -> AnimeList.UPCOMING_NEXT_SEASON
                4, -4 -> AnimeList.ALL_TIME_POPULAR
                5, -5 -> AnimeList.NEWLY_ADDED
                else -> null
            }
        }.orEmpty().filterNotNull().toImmutableList()
    }
    val animeListsIndices = preferencesRepository.animeListsIndices.filterNotNull()
    val showUserDescription = preferencesRepository.showUserDescription.filterNotNull()

    val mangaList = preferencesRepository.mangaListsIndices.map { indices ->
        indices?.map {
            // each int from +-1 to +-3 maps to a MangaList
            when (it.toInt()) {
                1, -1 -> MangaList.TRENDING_NOW
                2, -2 -> MangaList.ALL_TIME_POPULAR
                3, -3 -> MangaList.NEWLY_ADDED
                else -> null
            }
        }.orEmpty().filterNotNull().toImmutableList()
    }
    val mangaListsIndices = preferencesRepository.mangaListsIndices.filterNotNull()

    fun setTheme(theme: Theme) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setTheme(theme.name)
    }
    fun setUseSystemColorScheme(useSystemColorScheme: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setUseSystemColorScheme(useSystemColorScheme)
    }

    fun setIsAmoled(isAmoled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setIsAmoled(isAmoled)
    }

    fun setDensity(density: Density) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setDensity(density.name)
    }

    fun setIsNsfwEnabled(isNsfwEnabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setIsNsfwEnabled(isNsfwEnabled)
    }

    fun setLanguage(language: Media.Language) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setLanguage(language.name)
    }

    fun setListSize(listSize: Int) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setListSize(listSize)
    }

    fun setShowUserDescription(showUserDescription: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setShowUserDescription(showUserDescription)
    }

    fun setDevOptions(enabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setDevOptionsEnabled(enabled)
    }

    fun setDayHour(dayHour: Float?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setDayHour(dayHour?.coerceIn(0f, 23f))
    }

    fun setAnimeListsIndices(from: Int, to: Int) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setAnimeListsIndices(from, to)
    }

    fun toggleAnimeList(index: Int) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.toggleAnimeList(index)
    }

    fun resetAnimeLists() = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.resetAnimeLists()
    }

    fun setMangaListsIndices(from: Int, to: Int) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setMangaListsIndices(from, to)
    }

    fun toggleMangaList(index: Int) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.toggleMangaList(index)
    }

    fun resetMangaLists() = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.resetMangaLists()
    }
}
