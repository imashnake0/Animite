package com.imashnake.animite.media

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.core.ui.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MediaPageViewModel @Inject constructor(
    private val mediaRepository: AnilistMediaRepository,
    preferencesRepository: PreferencesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val navArgs = savedStateHandle.toRoute<MediaPage>()

    private val refreshTrigger = MutableSharedFlow<Unit>()

    val source = savedStateHandle.getStateFlow(Constants.SOURCE, navArgs.source)
    val id = savedStateHandle.getStateFlow(Constants.ID, navArgs.id)

    val listSize = preferencesRepository.listSize.filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 10
    )

    val media = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = preferencesRepository.language.filterNotNull(),
        flow3 = preferencesRepository.listSize.filterNotNull(),
        transform = ::Triple
    ).flatMapLatest { (_, language, listSize) ->
        // TODO: Remove after loading states are working.
        delay(3.seconds)
        mediaRepository.fetchMedia(
            id = navArgs.id,
            mediaType = MediaType.safeValueOf(navArgs.mediaType),
            perPage = listSize,
            language = Media.Language.valueOf(language)
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading()
    )
}
