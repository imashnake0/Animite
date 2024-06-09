package com.imashnake.animite.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistUserRepository
import com.imashnake.animite.api.ext.RefreshableFlow
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: AnilistUserRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val isLoggedIn = preferencesRepository
        .accessToken
        .map { !it.isNullOrEmpty() }

    val viewer = RefreshableFlow(
        viewModelScope = viewModelScope,
        minimumDelay = 250,
        fetchData = { userRepository.fetchViewer(it).asResource() },
        initialValue = Resource.loading()
    )

    fun setAccessToken(accessToken: String?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setAccessToken(accessToken)
    }
}
