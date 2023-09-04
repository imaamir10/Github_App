package com.example.githubapp.feature_github_user.domain.repository

import androidx.paging.PagingData
import com.example.githubapp.core.Resource

import com.example.githubapp.feature_github_user.domain.model.followers.ResponseFollowersItem
import com.example.githubapp.feature_github_user.domain.model.repo.RepoItem
import com.example.githubapp.feature_github_user.domain.model.repo.ResponseRepoList
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getPagingData(query: String): Flow<PagingData<UserItem>>
    suspend fun getRepoList(url :String): Flow<Resource<List<RepoItem>>>
    suspend fun getFollowerList(url: String): Flow<Resource<List<ResponseFollowersItem>>>
}