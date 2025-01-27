package com.imashnake.animite.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistUserRepository
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import com.imashnake.animite.navigation.ProfileRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: AnilistUserRepository,
    private val preferencesRepository: PreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = savedStateHandle.toRoute<ProfileRoute>()

    val isLoggedIn = preferencesRepository
        .accessToken
        .map { !it.isNullOrEmpty() }

    val viewer = userRepository
        .fetchViewer()
        .asResource()
        .onEach {
            it.data?.let { user -> preferencesRepository.setViewerId(user.id) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val viewerAnimeLists = preferencesRepository.viewerId.flatMapLatest {
        userRepository
            .fetchUserMediaList(it?.toIntOrNull(), MediaType.ANIME)
            .asResource()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val viewerMangaLists = preferencesRepository.viewerId.flatMapLatest {
        userRepository
            .fetchUserMediaList(it?.toIntOrNull(), MediaType.MANGA)
            .asResource()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    init {
        if (navArgs.accessToken != null) {
            viewModelScope.launch(Dispatchers.IO) {
                preferencesRepository.setAccessToken(navArgs.accessToken)
            }
        }
    }
}
