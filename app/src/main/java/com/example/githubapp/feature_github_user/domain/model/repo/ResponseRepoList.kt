package com.example.githubapp.feature_github_user.domain.model.repo

import com.example.githubapp.feature_github_user.domain.model.repo.RepoItem

data class ResponseRepoList(
    val incomplete_results: Boolean?= null,
    val items: List<RepoItem>?= null,
    val total_count: Int?= null
)