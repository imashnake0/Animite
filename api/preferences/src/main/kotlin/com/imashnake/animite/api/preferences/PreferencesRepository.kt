package com.imashnake.animite.api.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.imashnake.animite.api.preferences.ext.getValue
import com.imashnake.animite.api.preferences.ext.setValue
import javax.inject.Inject

private const val DEFAULT_THEME_KEY = "DEVICE_THEME"
private const val USE_SYSTEM_COLOR_SCHEME = true
private const val IS_DEV_OPTIONS_ENABLED = false

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
    private val themeKey = stringPreferencesKey("theme")
    val theme = dataStore.getValue(themeKey, DEFAULT_THEME_KEY)
    suspend fun setThemeToken(theme: String) {
        dataStore.setValue(themeKey, theme)
    }

    private val useSystemColorSchemeKey = booleanPreferencesKey("system_color_scheme")
    val useSystemColorScheme = dataStore.getValue(useSystemColorSchemeKey, USE_SYSTEM_COLOR_SCHEME)
    suspend fun setUseSystemColorScheme(useSystemColorScheme: Boolean) {
        dataStore.setValue(useSystemColorSchemeKey, useSystemColorScheme)
    }

    private val isDevOptionsEnabledKey = booleanPreferencesKey("dev_options_enabled")
    val isDevOptionsEnabled = dataStore.getValue(isDevOptionsEnabledKey, IS_DEV_OPTIONS_ENABLED)
    suspend fun setDevOptionsEnabled(isEnabled: Boolean) {
        dataStore.setValue(isDevOptionsEnabledKey, isEnabled)
    }
    // endregion
}
