package com.imashnake.animite.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistUserRepository
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import com.imashnake.animite.navigation.Root
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
class ProfileViewModel @AssistedInject constructor(
    @Assisted val navArgs: Root.ProfileRoute,
    private val userRepository: AnilistUserRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    private val refreshTrigger = MutableSharedFlow<Unit>()

    var useNetwork = false

    val isLoggedIn = preferencesRepository
        .accessToken
        .map { !it.isNullOrEmpty() }

    val viewer = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            userRepository.fetchViewer(useNetwork)
        }.asResource().onEach {
            it.data?.let { user -> preferencesRepository.setViewerId(user.id) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = Resource.loading(),
        )

    val viewerAnimeLists = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = preferencesRepository.viewerId.filterNotNull(),
        transform = ::Pair,
    ).flatMapLatest {
        userRepository.fetchUserMediaList(
            id = it.second.toIntOrNull(),
            type = MediaType.ANIME,
            useNetwork = useNetwork,
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val viewerMangaLists = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = preferencesRepository.viewerId.filterNotNull(),
        transform = ::Pair,
    ).flatMapLatest {
        userRepository.fetchUserMediaList(
            id = it.second.toIntOrNull(),
            type = MediaType.MANGA,
            useNetwork = useNetwork,
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    fun logOut() = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setAccessToken(null)
        preferencesRepository.setViewerId(null)
    }

    fun refresh(onRefresh: () -> Unit) = viewModelScope.launch {
        onRefresh()
        useNetwork = true
        refreshTrigger.emit(Unit)
        useNetwork = false
    }

    init {
        if (navArgs.accessToken != null) {
            viewModelScope.launch(Dispatchers.IO) {
                preferencesRepository.setAccessToken(navArgs.accessToken)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navArgs: Root.ProfileRoute): ProfileViewModel
    }
}
