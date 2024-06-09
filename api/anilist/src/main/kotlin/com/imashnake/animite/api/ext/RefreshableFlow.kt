package com.imashnake.animite.api.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Refreshing logic stolen from:
 * https://github.com/boswelja/NASdroid/blob/main/features/apps/ui/src/main/kotlin/com/nasdroid/apps/ui/installed/overview/InstalledAppsOverviewViewModel.kt
 */
class RefreshableFlow<T>(
    private val viewModelScope: CoroutineScope,
    private val minimumDelay: Long = 250,
    fetchData: (useNetwork: Boolean) -> Flow<T>,
    initialValue: T,
    coldToHot: Flow<T>.(initialValue: T) -> StateFlow<T> = {
        stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000),
            it
        )
    }
) {
    private val _refreshTrigger = MutableSharedFlow<Unit>()

    private var _useNetwork = false

    private val _refreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> = _refreshing

    @OptIn(ExperimentalCoroutinesApi::class)
    val data = _refreshTrigger
        .onStart { refresh() }
        .onEach { _refreshing.value = true }
        .flatMapLatest {
            if (_useNetwork) delay(minimumDelay)
            fetchData(_useNetwork)
        }
        .onEach { _refreshing.value = false }
        .onEach { setNetworkMode(false) }
        .coldToHot(initialValue)

    fun refresh() = viewModelScope.launch {
        _refreshTrigger.emit(Unit)
    }

    fun setNetworkMode(useNetwork: Boolean) {
        _useNetwork = useNetwork
    }
}
