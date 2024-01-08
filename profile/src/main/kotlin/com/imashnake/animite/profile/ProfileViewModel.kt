package com.imashnake.animite.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistUserRepository
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.GlobalVariables
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: AnilistUserRepository,
    private val preferencesRepository: PreferencesRepository,
    private val globalVariables: GlobalVariables,
) : ViewModel() {
    fun setAccessToken(accessToken: String?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setAccessToken(accessToken)
    }

    val accessToken = preferencesRepository
        .accessToken
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading<String?>())

    val viewer = userRepository
        .fetchViewer()
        .asResource()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    init {
        preferencesRepository.accessToken.onEach {
            globalVariables.accessToken = it
        }.launchIn(viewModelScope)
    }
}
