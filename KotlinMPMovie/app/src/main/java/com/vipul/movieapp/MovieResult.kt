package com.vipul.movieapp

sealed class MovieResult<out T> {

    data class Success<T>(val data: T) : MovieResult<T>()
    data class Error(val throwable: Throwable) : MovieResult<Nothing>()
    object Loading : MovieResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$throwable]"
            Loading -> "Loading"
        }
    }
}