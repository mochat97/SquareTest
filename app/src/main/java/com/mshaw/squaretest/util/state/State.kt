package com.mshaw.squaretest.util.state

sealed class State<out T> {
    object Loading: State<Nothing>()
    data class Success<T>(val value: T): State<T>()
    data class Error(val exception: Exception): State<Nothing>()
}