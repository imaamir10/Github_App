package com.example.githubapp.feature_github_user.presentation.callback

import com.example.githubapp.feature_github_user.domain.model.user.UserItem

interface OnUserItemClickListener {
    fun onUserItemClicked(userItem: UserItem)
}