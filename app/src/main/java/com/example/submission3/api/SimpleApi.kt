package com.example.submission3.api

import com.example.submission3.model.FollowersItem
import com.example.submission3.model.UserDetails
import com.example.submission3.model.UserItem
import com.example.submission3.model.UserQuery
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpleApi {
    @GET("users")
    suspend fun getUser(): Response<ArrayList<UserItem>>

    @GET("search/users")
    suspend fun getUserQuery(@Query("q") login: String): Response<UserQuery>

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserDetails>

    @GET("users/{username}/followers")
    suspend fun getUserFollower(@Path("username") username: String): Response<ArrayList<FollowersItem>>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(@Path("username") username: String): Response<ArrayList<FollowersItem>>
}