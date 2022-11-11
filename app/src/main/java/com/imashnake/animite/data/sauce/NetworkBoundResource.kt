package com.imashnake.animite.data.sauce

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

fun <API, DB> networkBoundResource(
    db: Flow<DB>,
    request: suspend () -> API,
    insert: suspend (API) -> Unit
): Flow<DB> = flow {
    runCatching {
        request()
    }.onSuccess {
        insert(it)
    }.onFailure {
        // throw exception
    }
    emitAll(db)
}