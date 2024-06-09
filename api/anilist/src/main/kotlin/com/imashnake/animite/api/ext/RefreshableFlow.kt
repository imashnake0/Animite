package com.imashnake.animite.api.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshableFlow<T>(
    private val viewModelScope: CoroutineScope,
    private val minimumDelay: Long,
    fetchData: (useNetwork: Boolean) -> Flow<T>,
    coldToHot: Flow<T>.() -> StateFlow<T>
) {
    private val _refreshTrigger = MutableSharedFlow<Unit>()

    private val _useNetwork = MutableStateFlow(false)

    private val _refreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> = _refreshing

    val data = _refreshTrigger
        .onStart { refresh() }
        .onEach { _refreshing.value = true }
        .flatMapLatest {
            if (_useNetwork.value) delay(minimumDelay)
            fetchData(_useNetwork.value)
        }
        .onEach { _refreshing.value = false }
        .onEach { setNetworkMode(false) }
        .coldToHot()

    fun refresh() = viewModelScope.launch {
        _refreshTrigger.emit(Unit)
    }

    fun setNetworkMode(useNetwork: Boolean) {
        _useNetwork.value = useNetwork
    }
}
