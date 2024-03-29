package com.imashnake.animite.api.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.imashnake.animite.api.preferences.ext.getValue
import com.imashnake.animite.api.preferences.ext.setValue
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    val accessToken = dataStore.getValue(accessTokenKey, null)

    suspend fun setAccessToken(accessToken: String?) {
        dataStore.setValue(accessTokenKey, accessToken)
    }
}
