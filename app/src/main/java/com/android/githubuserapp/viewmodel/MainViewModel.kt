package com.android.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.data.response.SearchResponse
import com.android.githubuserapp.networking.ApiConfig
import com.android.githubuserapp.utility.Converter.convertSearchToUser
import com.android.githubuserapp.utility.MainState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listSearchData = MutableLiveData<ArrayList<User>>()
    val listSearchData: LiveData<ArrayList<User>> = _listSearchData

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private fun stateLoading(isLoading: Boolean) {
        _state.value = MainState.Loading(isLoading)
    }

    private fun stateEmpty(isEmpty: Boolean) {
        _state.value = MainState.Empty(isEmpty)
    }

    private fun stateRecycler(isRecycler: Boolean) {
        _state.value = MainState.Recycler(isRecycler)
    }

    private fun stateServer(isError: Boolean, msg: String) {
        _state.value = MainState.ServerState(isError, msg)
    }

    fun setSearchData(query: String) {
        stateLoading(true)
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<SearchResponse> {

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                stateEmpty(true)
                val result = response.body()?.items
                if (response.isSuccessful && result != null) {
                    _listSearchData.postValue(convertSearchToUser(result))
                    stateLoading(false)
                    stateEmpty(false)
                    stateServer(false, response.code().toString())
                    stateRecycler(true)
                } else {
                    stateRecycler(false)
                    stateLoading(false)
                    stateEmpty(false)
                    stateServer(true, response.code().toString())
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                stateRecycler(false)
                stateLoading(false)
                stateEmpty(false)
                stateServer(true, t.message.toString())
            }
        })
    }
}