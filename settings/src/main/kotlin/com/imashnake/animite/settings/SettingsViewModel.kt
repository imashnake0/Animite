package com.imashnake.animite.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.preferences.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    val theme = preferencesRepository.theme

    fun setTheme(theme: THEME) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setThemeToken(theme.toPreferenceKey())
    }
}

private fun THEME.toPreferenceKey() = when(this) {
    THEME.DARK -> -1
    THEME.LIGHT -> 0
    THEME.DEVICE_THEME -> 1
}
