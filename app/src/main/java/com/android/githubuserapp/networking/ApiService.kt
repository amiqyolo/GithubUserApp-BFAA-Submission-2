package com.android.githubuserapp.networking

import com.android.githubuserapp.data.response.DetailResponse
import com.android.githubuserapp.data.response.FollowersFollowingResponse
import com.android.githubuserapp.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getSearchUser(@Query("q") query: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowersUser(@Path("username") username: String): Call<ArrayList<FollowersFollowingResponse>>

    @GET("users/{username}/following")
    fun getFollowingUser(@Path("username") username: String): Call<ArrayList<FollowersFollowingResponse>>
}