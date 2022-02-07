package com.android.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.data.response.FollowersFollowingResponse
import com.android.githubuserapp.networking.ApiConfig
import com.android.githubuserapp.utility.Converter.convertFollowersFollowingToUser
import com.android.githubuserapp.utility.MainState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _listFollowersData = MutableLiveData<ArrayList<User>>()
    val listFollowersData: LiveData<ArrayList<User>> = _listFollowersData

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private fun stateLoading(isLoading: Boolean) {
        _state.value = MainState.Loading(isLoading)
    }

    fun setFollowersData(query: String?) {
        stateLoading(true)
        val client = ApiConfig.getApiService().getFollowersUser(query!!)
        client.enqueue(object : Callback<ArrayList<FollowersFollowingResponse>> {
            override fun onResponse(
                call: Call<ArrayList<FollowersFollowingResponse>>,
                response: Response<ArrayList<FollowersFollowingResponse>>,
            ) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _listFollowersData.postValue(convertFollowersFollowingToUser(result))
                    stateLoading(false)
                } else {
                    stateLoading(false)
                }
            }

            override fun onFailure(
                call: Call<ArrayList<FollowersFollowingResponse>>,
                t: Throwable,
            ) {
                stateLoading(false)
            }
        })
    }
}