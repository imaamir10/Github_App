package com.example.githubapp.feature_github_user.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubapp.core.Resource
import com.example.githubapp.core.handleApiResponse
import com.example.githubapp.feature_github_user.data.remote.RetrofitApi
import com.example.githubapp.feature_github_user.domain.model.followers.ResponseFollowersItem
import com.example.githubapp.feature_github_user.domain.model.repo.ResponseRepoList
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import com.example.githubapp.feature_github_user.data.remote.paging.UserPagingSource
import com.example.githubapp.feature_github_user.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GithubRepositoryImpl(private val api: RetrofitApi) : GithubRepository {
    override fun getPagingData(query: String): Flow<PagingData<UserItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                prefetchDistance = 5,
                ),
            pagingSourceFactory = { UserPagingSource(api,query) }
        ).flow
    }

    override suspend fun getRepoList(url :String): Flow<Resource<List<ResponseRepoList>>> = handleApiResponse(
        apiCall = { api.getRepoList(url) },
        convert = { responseItem ->
            responseItem.map { it.toReponseRepoList() }
        }
    )

    override suspend fun getFollowerList(url :String): Flow<Resource<List<ResponseFollowersItem>>> = handleApiResponse(
        apiCall = {api.getFollowerList(url)},
        convert = { responseItem -> responseItem.map { it.toResponseFollowersItem() } }
    )


}