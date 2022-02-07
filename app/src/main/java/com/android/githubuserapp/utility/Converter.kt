package com.android.githubuserapp.utility

import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.data.response.DetailResponse
import com.android.githubuserapp.data.response.FollowersFollowingResponse
import com.android.githubuserapp.data.response.ItemsItem

object Converter {
    fun convertSearchToUser(searchResponse: ArrayList<ItemsItem>?): ArrayList<User> {
        val models = mutableListOf<User>()

        searchResponse?.forEach {
            it.apply {
                models.add(
                    User(
                        login = this.login,
                        avatarUrl = this.avatarUrl
                    )
                )
            }
        }

        return models as ArrayList<User>
    }

    fun convertDetailToUser(detailResponse: DetailResponse): User {
        return User(
            avatarUrl = detailResponse.avatarUrl,
            login = detailResponse.login,
            followers = detailResponse.followers,
            following = detailResponse.following,
            name = detailResponse.name,
            company = detailResponse.company,
            location = detailResponse.location,
            publicRepos = detailResponse.publicRepos
        )
    }

    fun convertFollowersFollowingToUser(
        followersFollowingResponse: ArrayList<FollowersFollowingResponse>?
    ): ArrayList<User> {
        val models = mutableListOf<User>()

        followersFollowingResponse?.forEach {
            it.apply {
                models.add(
                    User(
                        login = this.login,
                        avatarUrl = this.avatarUrl
                    )
                )
            }
        }

        return models as ArrayList<User>
    }
}