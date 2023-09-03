package com.example.githubapp.feature_github_user.data.remote.dto.user

import com.example.githubapp.feature_github_user.domain.model.user.ResponseUserList
import com.example.githubapp.feature_github_user.domain.model.user.UserItem

data class ResponseUserListDto(
    val incomplete_results: Boolean?= null,
    val items: List<UserItem>?= null,
    val total_count: Int?= null
) {
    fun toResponseUserList(): ResponseUserList {
        return ResponseUserList(
            items = items
        )
    }
}