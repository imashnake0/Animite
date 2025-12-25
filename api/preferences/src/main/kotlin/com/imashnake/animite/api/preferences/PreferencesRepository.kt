package com.imashnake.animite.api.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.imashnake.animite.api.preferences.ext.getValue
import com.imashnake.animite.api.preferences.ext.setValue
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // region profile
    private val accessTokenKey = stringPreferencesKey("access_token")
    val accessToken = dataStore.getValue(accessTokenKey, null)
    suspend fun setAccessToken(accessToken: String?) {
        dataStore.setValue(accessTokenKey, accessToken)
    }

    private val viewerIdKey = stringPreferencesKey("viewer_id")
    val viewerId = dataStore.getValue(viewerIdKey, null)
    suspend fun setViewerId(viewerId: Int?) {
        dataStore.setValue(viewerIdKey, viewerId.toString())
    }
    // endregion

    // region settings
    private val themeKey = intPreferencesKey("theme")
    val theme = dataStore.getValue(themeKey, null)
    suspend fun setThemeToken(theme: Int?) {
        dataStore.setValue(themeKey, theme)
    }
    // endregion
}
