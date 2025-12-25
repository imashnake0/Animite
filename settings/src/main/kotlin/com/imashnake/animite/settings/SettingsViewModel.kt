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
        preferencesRepository.setThemeToken(theme.name)
    }
}
