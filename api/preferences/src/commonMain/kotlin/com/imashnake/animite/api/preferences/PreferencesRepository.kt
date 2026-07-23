package com.imashnake.animite.api.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.imashnake.animite.api.preferences.ext.getValue
import com.imashnake.animite.api.preferences.ext.setValue
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

private const val DEFAULT_THEME_KEY = "DEVICE_THEME"
private const val DEFAULT_DENSITY_KEY = "COMFY"
private const val USE_SYSTEM_COLOR_SCHEME = true
private const val IS_AMOLED = false
private const val IS_NSFW_ENABLED = false
private const val DEFAULT_LANGUAGE_KEY = "DEFAULT"
private const val SHOW_USER_DESCRIPTION = true
private const val IS_DEV_OPTIONS_ENABLED = false

/**
 * Stores API-specific state in an on-disk cache, and exposes the values as a Flow.
 */
class PreferencesRepository internal constructor(
    private val dataStore: DataStore<Preferences>
) {
    // region anime
    private val animeListsIndicesKey = byteArrayPreferencesKey("anime_lists_indices")
    val animeListsIndices = dataStore.getValue(animeListsIndicesKey, byteArrayOf(1, 2, 3, 4, 5))
    suspend fun setAnimeListsIndices(from: Int, to: Int) {
        dataStore.setValue(
            animeListsIndicesKey,
            animeListsIndices.firstOrNull()?.toMutableList()?.apply {
                add(to, removeAt(from))
            }?.toByteArray()
        )
    }
    suspend fun toggleAnimeList(index: Int) {
        dataStore.setValue(
            animeListsIndicesKey,
            animeListsIndices.firstOrNull()?.toMutableList()?.apply {
                set(index, (get(index) * -1).toByte())
            }?.toByteArray()
        )
    }
    suspend fun resetAnimeLists() {
        dataStore.setValue(animeListsIndicesKey, byteArrayOf(1, 2, 3, 4, 5))
    }
    // endregion

    // region manga
    private val mangaListsIndicesKey = byteArrayPreferencesKey("manga_lists_indices")
    val mangaListsIndices = dataStore.getValue(mangaListsIndicesKey, byteArrayOf(1, 2, 3))
    suspend fun setMangaListsIndices(from: Int, to: Int) {
        dataStore.setValue(
            mangaListsIndicesKey,
            mangaListsIndices.firstOrNull()?.toMutableList()?.apply {
                add(to, removeAt(from))
            }?.toByteArray()
        )
    }
    suspend fun toggleMangaList(index: Int) {
        dataStore.setValue(
            mangaListsIndicesKey,
            mangaListsIndices.firstOrNull()?.toMutableList()?.apply {
                set(index, (get(index) * -1).toByte())
            }?.toByteArray()
        )
    }
    suspend fun resetMangaLists() {
        dataStore.setValue(mangaListsIndicesKey, byteArrayOf(1, 2, 3))
    }
    // endregion

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

    private val animeListOrderKey = stringPreferencesKey("anime_list_order")
    val animeListOrder = dataStore.getValue(animeListOrderKey, null)
    suspend fun setAnimeListOrder(animeListOrder: List<String>?) {
        dataStore.setValue(animeListOrderKey, Json.encodeToString(animeListOrder))
    }

    private val mangaListOrderKey = stringPreferencesKey("manga_list_order")
    val mangaListOrder = dataStore.getValue(mangaListOrderKey, null)
    suspend fun setMangaListOrder(mangaListOrder: List<String>?) {
        dataStore.setValue(mangaListOrderKey, Json.encodeToString(mangaListOrder))
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
    suspend fun setTheme(theme: String) {
        dataStore.setValue(themeKey, theme)
    }

    private val useSystemColorSchemeKey = booleanPreferencesKey("system_color_scheme")
    val useSystemColorScheme = dataStore.getValue(useSystemColorSchemeKey, USE_SYSTEM_COLOR_SCHEME)
    suspend fun setUseSystemColorScheme(useSystemColorScheme: Boolean) {
        dataStore.setValue(useSystemColorSchemeKey, useSystemColorScheme)
    }

    private val isAmoledKey = booleanPreferencesKey("is_amoled")
    val isAmoled = dataStore.getValue(isAmoledKey, IS_AMOLED)
    suspend fun setIsAmoled(isAmoled: Boolean) {
        dataStore.setValue(isAmoledKey, isAmoled)
    }

    private val densityKey = stringPreferencesKey("density")
    val density = dataStore.getValue(densityKey, DEFAULT_DENSITY_KEY)
    suspend fun setDensity(density: String) {
        dataStore.setValue(densityKey, density)
    }

    private val isNsfwEnabledKey = booleanPreferencesKey("is_nsfw_enabled")
    val isNsfwEnabled = dataStore.getValue(isNsfwEnabledKey, IS_NSFW_ENABLED)
    suspend fun setIsNsfwEnabled(isNsfwEnabled: Boolean) {
        dataStore.setValue(isNsfwEnabledKey, isNsfwEnabled)
    }

    private val languageKey = stringPreferencesKey("language")
    val language = dataStore.getValue(languageKey, DEFAULT_LANGUAGE_KEY)
    suspend fun setLanguage(language: String) {
        dataStore.setValue(languageKey, language)
    }

    private val listSizeKey = intPreferencesKey("list_size")
    val listSize = dataStore.getValue(listSizeKey, 10)
    suspend fun setListSize(listSize: Int) {
        dataStore.setValue(listSizeKey, listSize)
    }

    private val showUserDescriptionKey = booleanPreferencesKey("show_user_description")
    val showUserDescription = dataStore.getValue(showUserDescriptionKey, SHOW_USER_DESCRIPTION)
    suspend fun setShowUserDescription(showUserDescription: Boolean) {
        dataStore.setValue(showUserDescriptionKey, showUserDescription)
    }

    // region developer options
    private val isDevOptionsEnabledKey = booleanPreferencesKey("dev_options_enabled")
    val isDevOptionsEnabled = dataStore.getValue(isDevOptionsEnabledKey, IS_DEV_OPTIONS_ENABLED)
    suspend fun setDevOptionsEnabled(isEnabled: Boolean) {
        if (!isEnabled) {
            dataStore.setValue(dayHourKey, null)
        }
        dataStore.setValue(isDevOptionsEnabledKey, isEnabled)
    }

    private val dayHourKey = floatPreferencesKey("day_hour")
    val dayHour = dataStore.getValue(dayHourKey, null)
    suspend fun setDayHour(dayHour: Float?) {
        dataStore.setValue(dayHourKey, dayHour)
    }
    // endregion

    // endregion

    companion object
}
