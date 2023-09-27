package com.rival.githubusersapp.data.network

import com.rival.githubusersapp.data.model.GithubItemUser
import com.rival.githubusersapp.data.model.GithubResponse
import com.rival.githubusersapp.data.model.UserDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/search/users")
    fun searchUser(@Query("q") username: String?): Call<GithubResponse>

    @GET("/users/{username}")
    fun getDetailUser(@Path("username") username: String?): Call<UserDetailResponse>

    @GET("/users/{username}/followers")
    fun getFollowers(@Path("username") username: String?): Call<List<GithubItemUser>>

    @GET("/users/{username}/following")
    fun getFollowing(@Path("username") username: String?): Call<List<GithubItemUser>>

}