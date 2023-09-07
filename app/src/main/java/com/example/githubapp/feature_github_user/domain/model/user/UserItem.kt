package com.example.githubapp.feature_github_user.domain.model.user

data class UserItem(
    val avatar_url: String ?= null,
    val followers_url: String ?= null,
    val id: Int ?= null,
    val login: String ?= null,
    val repos_url: String?= null,
)