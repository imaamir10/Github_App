package com.example.githubapp.feature_github_user.domain.usecase

import com.example.githubapp.feature_github_user.domain.repository.GithubRepository

class GetFollowersUseCase(private val repository: GithubRepository) {
    operator fun invoke() = repository.getFollowerList()
}