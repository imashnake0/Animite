package com.imashnake.animite.core.resource

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest

public sealed class Resource<T> private constructor(
    public open val data: T?,
    public open val message: String? = null
) {

    public data class Success<T>(override val data: T) : Resource<T>(data)
    public data class Error<T>(override val message: String?, override val data: T? = null) : Resource<T>(data, message)
    public class Loading<T> : Resource<T>(null)

    public companion object {
        public fun <T> success(data: T): Resource<T> = Success(data)

        public fun <T> error(msg: String, data: T? = null): Resource<T> = Error(msg, data)

        public fun <T> loading(): Resource<T> = Loading()

        @OptIn(ExperimentalCoroutinesApi::class)
        public fun <T, R> Flow<Result<T>>.asResource(transform: (T) -> R): Flow<Resource<R>> = this
            .mapLatest {
                success(transform(it.getOrThrow()))
            }
            .catch {
                emit(error(it.message.orEmpty()))
            }

        public fun <T> Flow<Result<T>>.asResource(): Flow<Resource<T>> = this.asResource { it }
    }
}
