package com.example.githubapp.feature_github_user.data.remote

import com.example.githubapp.core.USER_ENDPOINT
import com.example.githubapp.feature_github_user.data.remote.dto.followers.ResponseFollowersItemDto
import com.example.githubapp.feature_github_user.data.remote.dto.repo.RepoItemDto
import com.example.githubapp.feature_github_user.data.remote.dto.user.ResponseUserListDto

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitApi {

    @GET(USER_ENDPOINT)
    suspend fun getGetUser(
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
        @Query("q") query: String
    ): Response<ResponseUserListDto>

    @GET
    suspend fun getRepoList(@Url dynamicEndpoint: String): Response<List<RepoItemDto>>
    @GET
    suspend fun getFollowerList(@Url dynamicEndpoint: String): Response<List<ResponseFollowersItemDto>>
}