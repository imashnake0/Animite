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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: AnilistUserRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val isLoggedIn = preferencesRepository
        .accessToken
        .map { !it.isNullOrEmpty() }

    val viewer = userRepository
        .fetchViewer()
        .asResource()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val viewerMediaList = preferencesRepository.viewerId.flatMapLatest {
        userRepository
            .fetchUserMediaList(it?.toIntOrNull())
            .asResource()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())
    }

    fun setAccessToken(accessToken: String?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setAccessToken(accessToken)
    }

    fun setViewerId(viewerId: Int?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setViewerId(viewerId)
    }
}
