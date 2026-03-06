package com.imashnake.animite.api.preferences.domain

import com.imashnake.animite.api.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveThemeUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) {

    operator fun invoke(): Flow<Theme?> {
        return preferencesRepository.theme
            .filterNotNull()
            .map { theme -> Theme.valueOf(theme) }
    }
}
