package com.imashnake.animite.api.anilist

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
fun <T: Operation.Data, R> Flow<ApolloResponse<T>>.asResult(transform: (T) -> R): Flow<Result<R>> = this
    .mapLatest {
        // TODO Is there a better way to handle errors here?
        it.data?.let { data ->
            Result.success(transform(data))
        } ?: Result.failure(IOException(it.errors?.firstOrNull()?.message.orEmpty()))
    }

fun <T: Operation.Data> Flow<ApolloResponse<T>>.asResult(): Flow<Result<T>> = this.asResult { it }
