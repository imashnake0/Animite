package com.imashnake.animite.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistUserRepository
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Refreshing logic stolen from:
 * https://github.com/boswelja/NASdroid/blob/main/features/apps/ui/src/main/kotlin/com/nasdroid/apps/ui/installed/overview/InstalledAppsOverviewViewModel.kt
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: AnilistUserRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _refreshTrigger = MutableSharedFlow<Unit>()

    private val _useNetwork = MutableStateFlow(false)

    private val _refreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _refreshing

    val isLoggedIn = preferencesRepository
        .accessToken
        .map { !it.isNullOrEmpty() }

    val viewer = _refreshTrigger
        .onStart { refresh() }
        .onEach { _refreshing.value = true }
        .flatMapLatest {
            if (_useNetwork.value) delay(500)
            userRepository.fetchViewer(_useNetwork.value).asResource()
        }
        .onEach { _refreshing.value = false }
        .onEach { setNetworkMode(false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    fun setAccessToken(accessToken: String?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setAccessToken(accessToken)
    }

    fun refresh() = viewModelScope.launch {
        _refreshTrigger.emit(Unit)
    }

    fun setNetworkMode(useNetwork: Boolean) {
        _useNetwork.value = useNetwork
    }
}
