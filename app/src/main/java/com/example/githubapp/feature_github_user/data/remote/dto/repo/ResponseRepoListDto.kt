package com.example.githubapp.feature_github_user.data.remote.dto.repo

import com.example.githubapp.feature_github_user.domain.model.repo.RepoItem
import com.example.githubapp.feature_github_user.domain.model.repo.ResponseRepoList

data class ResponseRepoListDto(
    val incomplete_results: Boolean?= null,
    val items: List<RepoItem>?= null,
    val total_count: Int?= null
){
    fun toReponseRepoList(): ResponseRepoList {
        return ResponseRepoList(
            items = items
        )
    }
}