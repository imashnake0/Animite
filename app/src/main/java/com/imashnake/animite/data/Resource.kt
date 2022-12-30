package com.imashnake.animite.data

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloCompositeException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.CacheMissException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

sealed class Resource<T>(
    open val data: T?,
    open val message: String? = null
) {

    data class Success<T>(override val data: T) : Resource<T>(data)
    data class Error<T>(override val message: String?, override val data: T? = null) : Resource<T?>(data, message)
    class Loading<T> : Resource<T>(null)

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Success(data)
        }

        fun <T> error(msg: String, data: T? = null): Resource<T?> {
            return Error(msg, data)
        }

        fun <T> loading(): Resource<T> {
            return Loading()
        }

        /**
         * Parses an [ApolloCall] into a [Resource] by checking its data & errors
         * As well as catching non-HTTP exceptions like no hostname for no connection & cache miss for cache-only requests
         *
         * TODO: Should require a mapper rather than a higher order lambda to provide access to the [ApolloResponse.data]
         */
        fun <T : Operation.Data, R> Flow<ApolloResponse<T>>.asResource(mapper: (T) -> R = { it as R }): Flow<Resource<R?>> {
            return map { response ->
                if (response.data != null) {
                    success<R?>(mapper(response.dataAssertNoErrors))
                } else if (response.hasErrors()) {
                    error(response.errors!!.joinToString { it.toString() })
                } else {
                    noDataError()
                }
            }.catch { e ->
                if (e is ApolloCompositeException) {
                    val (first, second) = e.suppressedExceptions
                    if (first is ApolloNetworkException || first is CacheMissException) {
                        return@catch emit(networkError())
                    } else if (second is ApolloNetworkException || second is CacheMissException) {
                        return@catch emit(networkError())
                    }
                }
                return@catch emit(defaultError())
            }
        }

        private fun <T> networkError() = error<T>("Network error, please check your connection and try again.")
        private fun <T> defaultError() = error<T>("Unknown error occurred, please try again later.")
        private fun <T> noDataError() = error<T>("Unknown error occurred, no data or errors received")
    }
}
