package com.android.githubuserapp.utility

sealed class MainState {
    data class Loading(val isLoading: Boolean) : MainState()
    data class Empty(val isEmpty: Boolean) : MainState()
    data class Recycler(val isRecycler: Boolean) : MainState()
    data class ServerState(val isError: Boolean, val message: String) : MainState()
}
