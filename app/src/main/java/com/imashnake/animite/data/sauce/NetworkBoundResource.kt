package com.imashnake.animite.data.sauce

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

fun <API, DB> networkBoundResource(
    db: Flow<DB>,
    request: suspend () -> API,
    insert: suspend (API) -> Unit
): Flow<DB> = db
    .onStart {
        // attempt non blocking fetch + save
        // not sure I'm a fan of this approach
        coroutineScope {
            runCatching {
                request()
            }.fold(
                onSuccess = { insert(it) },
                onFailure = Throwable::printStackTrace
            )
        }
    }
    .flowOn(Dispatchers.IO)