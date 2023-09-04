package com.example.githubapp.core

sealed class UIState<out T> {
    data class Loading(val isLoading: Boolean) : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}