package com.imashnake.animite.profile

import androidx.lifecycle.ViewModel
import com.imashnake.animite.api.preferences.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository
): ViewModel() {

    val viewerAvatar = preferencesRepository.viewerAvatar
}