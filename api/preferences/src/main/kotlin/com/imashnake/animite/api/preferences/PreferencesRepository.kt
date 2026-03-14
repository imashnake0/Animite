package com.imashnake.animite.api.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.imashnake.animite.api.preferences.ext.getValue
import com.imashnake.animite.api.preferences.ext.setValue

private const val DEFAULT_THEME_KEY = "DEVICE_THEME"
private const val USE_SYSTEM_COLOR_SCHEME = true
private const val IS_DEV_OPTIONS_ENABLED = false

/**
 * Stores API-specific state in an on-disk cache, and exposes the values as a Flow.
 */
class PreferencesRepository internal constructor(
    private val dataStore: DataStore<Preferences>
) {
    constructor(context: Context) : this(
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("default") }
        )
    )

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

    private val viewerAvatarKey = stringPreferencesKey("viewer_avatar")
    val viewerAvatar = dataStore.getValue(viewerAvatarKey, null)
    suspend fun setViewerAvatar(avatarUrl: String?) {
        dataStore.setValue(viewerAvatarKey, avatarUrl)
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
        if (!isEnabled) {
            dataStore.setValue(dayHourKey, null)
        }
        dataStore.setValue(isDevOptionsEnabledKey, isEnabled)
    }

    // region developer options
    private val dayHourKey = floatPreferencesKey("day_hour")
    val dayHour = dataStore.getValue(dayHourKey, null)
    suspend fun setDayHour(dayHour: Float?) {
        dataStore.setValue(dayHourKey, dayHour)
    }
    // endregion

    // endregion
}
