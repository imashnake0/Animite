package com.imashnake.animite.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.model.AnimeLists
import com.imashnake.animite.core.ui.Density
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val isDevOptionsEnabled = preferencesRepository.isDevOptionsEnabled.filterNotNull()
    val dayHour = preferencesRepository.dayHour
    val animeLists = preferencesRepository.animeListsIndices.map { indices ->
        indices?.map {
            // each int from 0 to 4 maps to a AnimeList
            when (it.toInt()) {
                0 -> AnimeLists.TRENDING_NOW
                1 -> AnimeLists.POPULAR_THIS_SEASON
                2 -> AnimeLists.UPCOMING_NEXT_SEASON
                3 -> AnimeLists.ALL_TIME_POPULAR
                4 -> AnimeLists.NEWLY_ADDED
                else -> null
            }
        }
    }

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

    fun setDevOptions(enabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setDevOptionsEnabled(enabled)
    }

    fun setDayHour(dayHour: Float?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setDayHour(dayHour?.coerceIn(0f, 23f))
    }

    fun setAnimeListsIndices(from: Int, to: Int) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setAnimeListsIndices(from, to)
    }
}
