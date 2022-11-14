package com.imashnake.animite.data.sauce

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

fun <API, DB> networkBoundResource(
    db: Flow<DB>,
    request: suspend () -> API,
    insert: suspend (API) -> Unit
): Flow<DB> = flow {

    val flow = db
        .onStart {
            runCatching {
                request()
            }.fold(
                onSuccess = { insert(it) },
                onFailure = Throwable::printStackTrace
            )
        }

    emitAll(flow)
}