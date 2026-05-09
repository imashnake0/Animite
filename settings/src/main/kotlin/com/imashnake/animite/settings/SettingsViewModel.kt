package com.imashnake.animite.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.ui.Density
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
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
}
