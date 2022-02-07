package com.android.githubuserapp.data.response

import com.google.gson.annotations.SerializedName

data class ItemsItem(
    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("login")
    val login: String,
)
