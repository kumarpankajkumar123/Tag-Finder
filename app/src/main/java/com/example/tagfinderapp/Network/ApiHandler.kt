package com.example.tagfinderapp.Network

sealed class ApiHandler<T> {
    data class Success<T>(val data : T): ApiHandler<T>()
    data class Failure<T>(val message : String): ApiHandler<T>()
    class Loading<T> : ApiHandler<T>()
}