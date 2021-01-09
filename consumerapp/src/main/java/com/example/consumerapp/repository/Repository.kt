package com.example.consumerapp.repository


import com.example.consumerapp.api.RetrofitInstance
import com.example.consumerapp.model.FollowersItem
import com.example.consumerapp.model.UserDetails
import com.example.consumerapp.model.UserItem
import com.example.consumerapp.model.UserQuery
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