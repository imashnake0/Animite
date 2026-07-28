package com.imashnake.animite.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistUserRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.navigation.ProfileRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
import kotlinx.serialization.json.Json
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: AnilistUserRepository,
    private val preferencesRepository: PreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs = savedStateHandle.toRoute<ProfileRoute>()
    private val refreshTrigger = MutableSharedFlow<Unit>()
    private val animeListsRefreshTrigger = MutableSharedFlow<Unit>()
    private val mangaListsRefreshTrigger = MutableSharedFlow<Unit>()

    var useNetwork = false

    val isLoggedIn = preferencesRepository
        .accessToken
        .map { !it.isNullOrEmpty() }

    val useProfileColor = preferencesRepository.useProfileColor.filterNotNull()
    val profileColor = preferencesRepository.profileColor.filterNotNull()

    val viewer = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = preferencesRepository.language.filterNotNull(),
        transform = ::Pair
    ).flatMapLatest {
        userRepository.fetchViewer(useNetwork, Media.Language.valueOf(it.second))
    }.asResource().onEach {
        it.data?.let { user ->
            // Don't update with cache
            if (useNetwork) {
                preferencesRepository.setViewerId(user.id)
                preferencesRepository.setProfileColor(user.color)
                preferencesRepository.setAnimeListOrder(user.animeListOrder)
                preferencesRepository.setMangaListOrder(user.mangaListOrder)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val viewerAnimeLists = combine(
        flow = animeListsRefreshTrigger.onStart { emit(Unit) },
        flow2 = combine(
            preferencesRepository.viewerId.filterNotNull(),
            preferencesRepository.language.filterNotNull(),
            ::Pair
        ),
        flow3 = preferencesRepository.animeListOrder.filterNotNull().map {
            Json.decodeFromString<List<String>>(it)
        },
        transform = ::Triple,
    ).flatMapLatest {
        userRepository.fetchUserMediaList(
            id = it.second.first.toIntOrNull(),
            type = MediaType.ANIME,
            useNetwork = useNetwork,
            language = Media.Language.valueOf(it.second.second),
            mediaListOrder = it.third
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val viewerMangaLists = combine(
        flow = mangaListsRefreshTrigger.onStart { emit(Unit) },
        flow2 = combine(
            preferencesRepository.viewerId.filterNotNull(),
            preferencesRepository.language.filterNotNull(),
            ::Pair
        ),
        flow3 = preferencesRepository.mangaListOrder.filterNotNull().map {
            Json.decodeFromString<List<String>>(it)
        },
        transform = ::Triple,
    ).flatMapLatest {
        userRepository.fetchUserMediaList(
            id = it.second.first.toIntOrNull(),
            type = MediaType.MANGA,
            useNetwork = useNetwork,
            language = Media.Language.valueOf(it.second.second),
            mediaListOrder = it.third
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    fun updateAnimeLists(sectionOrder: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateUser(animeSectionOrder = sectionOrder).collect {
            preferencesRepository.setAnimeListOrder(it.getOrNull()?.mediaListOptions?.animeList?.sectionOrder?.filterNotNull())
        }
        refreshAnimeLists()
    }

    fun updateMangaLists(sectionOrder: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateUser(mangaSectionOrder = sectionOrder).collect {
            preferencesRepository.setMangaListOrder(it.getOrNull()?.mediaListOptions?.mangaList?.sectionOrder?.filterNotNull())
        }
        refreshMangaLists()
    }

    private fun refreshAnimeLists() = viewModelScope.launch(Dispatchers.IO) {
        useNetwork = true
        animeListsRefreshTrigger.emit(Unit)
        useNetwork = false
    }

    private fun refreshMangaLists() = viewModelScope.launch(Dispatchers.IO) {
        useNetwork = true
        mangaListsRefreshTrigger.emit(Unit)
        useNetwork = false
    }

    fun refresh(
        shouldDelay: Boolean = true,
        setIsRefreshing: (Boolean) -> Unit,
    ) = viewModelScope.launch(Dispatchers.IO) {
        setIsRefreshing(true)
        useNetwork = true
        refreshTrigger.emit(Unit)
        animeListsRefreshTrigger.emit(Unit)
        mangaListsRefreshTrigger.emit(Unit)
        if (shouldDelay) delay(1500L)
        useNetwork = false
        setIsRefreshing(false)
    }

    fun logOut() = viewModelScope.launch(Dispatchers.IO) {
        with(preferencesRepository) {
            setAccessToken(null)
            setViewerId(null)
            setViewerAvatar(null)
        }
    }

    val viewerAvatar = preferencesRepository.viewerAvatar
    fun saveViewerAvatar(avatarUrl: String?) = viewModelScope.launch(Dispatchers.IO) {
        preferencesRepository.setViewerAvatar(avatarUrl)
    }

    init {
        if (navArgs.accessToken != null) {
            viewModelScope.launch(Dispatchers.IO) {
                preferencesRepository.setAccessToken(navArgs.accessToken)
            }
        }
    }
}
