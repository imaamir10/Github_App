package com.example.githubapp.feature_github_user.domain.model.user

import com.example.githubapp.feature_github_user.domain.model.user.UserItem

data class ResponseUserList(
    val incomplete_results: Boolean?= null,
    val items: List<UserItem>?= null,
    val total_count: Int?= null
)
