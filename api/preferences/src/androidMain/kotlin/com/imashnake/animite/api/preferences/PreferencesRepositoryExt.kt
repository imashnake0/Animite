package com.imashnake.animite.api.preferences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

/**
 * Creates a [PreferencesRepository] from the provided Android [Context].
 */
fun PreferencesRepository.Companion.create(context: Context): PreferencesRepository {
    return PreferencesRepository(
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("default") }
        )
    )
}
