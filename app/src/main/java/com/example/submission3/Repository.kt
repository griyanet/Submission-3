package com.example.submission3


import com.example.submission3.api.RetrofitInstance
import com.example.submission3.model.FollowersItem
import com.example.submission3.model.UserDetails
import com.example.submission3.model.UserItem
import com.example.submission3.model.UserQuery
import retrofit2.Response

class Repository {

    suspend fun getUser(): Response<ArrayList<UserItem>> {
        return RetrofitInstance.api.getUser()
    }

    suspend fun getUserQuery(username: String): Response<UserQuery> {
        return RetrofitInstance.api.getUserQuery(username)
    }

    suspend fun getUserDetail(username: String): Response<UserDetails> {
        return RetrofitInstance.api.getUserDetail(username)
    }

    suspend fun getUserFollower(username: String): Response<ArrayList<FollowersItem>> {
        return RetrofitInstance.api.getUserFollower(username)
    }

    suspend fun getUserFollowing(username: String): Response<ArrayList<FollowersItem>> {
        return RetrofitInstance.api.getUserFollowing(username)
    }
}