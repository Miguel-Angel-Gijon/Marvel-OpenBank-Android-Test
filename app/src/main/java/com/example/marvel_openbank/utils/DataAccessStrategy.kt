package com.example.marvel_openbank.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.marvel_openbank.utils.Resource.Status.*
import kotlinx.coroutines.Dispatchers

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)
        val responseStatus = networkCall.invoke()
        when (responseStatus.status) {
            SUCCESS -> {
                saveCallResult(responseStatus.data!!)
            }
            ERROR -> {
                emit(Resource.error(responseStatus.message!!))
                emitSource(source)
            }
            LOADING -> {
            }
        }
    }
