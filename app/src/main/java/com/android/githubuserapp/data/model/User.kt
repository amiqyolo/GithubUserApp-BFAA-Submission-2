package com.android.githubuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val avatarUrl: String,
    val login: String,
    val followers: Int?,
    val following: Int?,
    val name: String?,
    val company: String?,
    val location: String?,
    val publicRepos: Int?
) : Parcelable {
    constructor(
        avatarUrl: String,
        login: String
    ) : this(avatarUrl, login, 0, 0, null, null, null, 0)
}
