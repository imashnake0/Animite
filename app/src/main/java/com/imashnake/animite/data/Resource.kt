package com.imashnake.animite.data

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
    }
}