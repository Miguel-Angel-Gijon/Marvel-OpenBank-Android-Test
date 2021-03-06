package com.example.marvel_openbank.utils

import com.example.marvel_openbank.utils.Resource.Status.*

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        SUCCESS_UPDATE
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> successUpdate(data: T): Resource<T> {
            return Resource(SUCCESS_UPDATE, data, null)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}