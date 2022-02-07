package com.android.githubuserapp.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("items")
    val items: ArrayList<ItemsItem>
)
