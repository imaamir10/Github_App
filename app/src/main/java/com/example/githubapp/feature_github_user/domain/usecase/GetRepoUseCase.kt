package com.example.githubapp.feature_github_user.domain.usecase

import com.example.githubapp.feature_github_user.domain.repository.GithubRepository

class GetRepoUseCase(private val repository: GithubRepository) {
    suspend operator fun invoke(url :String) = repository.getRepoList(url)
}