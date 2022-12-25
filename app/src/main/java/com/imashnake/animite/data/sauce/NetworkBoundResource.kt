package com.imashnake.animite.data.sauce

import com.imashnake.animite.data.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


// TODO improve the handling of this, it's a bit too basic
fun <T> Flow<T>.asState(): Flow<Resource<T?>> {
    return map {
        Resource.success<T?>(it)
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "$exception"))
    }
}