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

class FollowingViewModel : ViewModel() {

    private val _listFollowingData = MutableLiveData<ArrayList<User>>()
    val listFollowingData: LiveData<ArrayList<User>> = _listFollowingData

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private fun stateLoading(isLoading: Boolean) {
        _state.value = MainState.Loading(isLoading)
    }

    fun setFollowingData(query: String?) {
        stateLoading(true)
        val client = ApiConfig.getApiService().getFollowingUser(query!!)
        client.enqueue(object : Callback<ArrayList<FollowersFollowingResponse>> {
            override fun onResponse(
                call: Call<ArrayList<FollowersFollowingResponse>>,
                response: Response<ArrayList<FollowersFollowingResponse>>
            ) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _listFollowingData.postValue(convertFollowersFollowingToUser(result))
                    stateLoading(false)
                } else {
                    stateLoading(false)
                }
            }

            override fun onFailure(
                call: Call<ArrayList<FollowersFollowingResponse>>,
                t: Throwable
            ) {
                stateLoading(false)
            }
        })
    }
}