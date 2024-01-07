package com.imashnake.animite.api.preferences.ext

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

fun <T> DataStore<Preferences>.getValue(
    key: Preferences.Key<T>,
    default: T? = null
) = data.map {
    it[key] ?: default
}

suspend fun <T> DataStore<Preferences>.setValue(
    key: Preferences.Key<T>,
    value: T?
) = edit {
    if (value != null) it[key] = value
    else it.remove(key)
}
