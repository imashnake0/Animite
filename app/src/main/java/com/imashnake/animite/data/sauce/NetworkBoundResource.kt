package com.imashnake.animite.data.sauce

import com.imashnake.animite.data.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun <API, DB> networkBoundResource(
    db: Flow<DB>,
    request: suspend () -> API,
    insert: suspend (API) -> Unit
): Flow<Resource<DB>> {
    return channelFlow {
        // nested scope to not block the emissions of the DB while we're fetching from API
        launch {
            runCatching {
                insert(request())
            }.onFailure { exception ->
                if (exception is CancellationException) { // our flow has been cancelled, we wanna cancel this nested scope too
                    throw exception
                }
                val current = db.firstOrNull() // snapshot of current DB to show in UI
                send(Resource.error(exception.message, current))
            }
            // TODO look into retry mechanisms here?
        }

        // emit changes from DB listener
        db.map { Resource.success(it) }
            .onEach(::send)
            .collect()
    }
}