package com.android.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.data.response.DetailResponse
import com.android.githubuserapp.networking.ApiConfig
import com.android.githubuserapp.utility.Converter.convertDetailToUser
import com.android.githubuserapp.utility.MainState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _listDetailData = MutableLiveData<User>()
    val listDetailData: LiveData<User> = _listDetailData

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private fun stateLoading(isLoading: Boolean) {
        _state.value = MainState.Loading(isLoading)
    }

    fun setDetailData(username: String) {
        stateLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _listDetailData.postValue(convertDetailToUser(result))
                    stateLoading(false)
                } else {
                    stateLoading(false)
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                stateLoading(false)
            }
        })
    }
}