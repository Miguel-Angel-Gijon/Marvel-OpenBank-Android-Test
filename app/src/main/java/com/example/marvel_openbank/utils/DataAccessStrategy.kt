package com.example.marvel_openbank.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.marvel_openbank.utils.Resource.Status.*
import kotlinx.coroutines.Dispatchers

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit,
    reCall: Boolean = false
): LiveData<Resource<T>> =
    if (!reCall) {
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            val source = databaseQuery.invoke().map { Resource.success(it) }
            emitSource(source)
            runResposeStatus(networkCall, saveCallResult, this)
        }
    } else {
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            runResposeStatus(networkCall, saveCallResult, this)
        }
    }

suspend fun <T, A> runResposeStatus(
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit,
    liveDataScope: LiveDataScope<Resource<T>>
) {
    val responseStatus = networkCall.invoke()
    when (responseStatus.status) {
        SUCCESS -> {
            saveCallResult(responseStatus.data!!)
        }
        ERROR -> {
            liveDataScope.emit(Resource.error(responseStatus.message!!))
        }
        LOADING -> {
        }
    }
}