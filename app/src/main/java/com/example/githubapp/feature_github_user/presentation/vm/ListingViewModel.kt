package com.example.githubapp.feature_github_user.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import com.example.githubapp.feature_github_user.domain.usecase.GetUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val useCase: GetUserListUseCase) :ViewModel(){


    private val currentQuery = MutableLiveData<String>()


    val pagingData: LiveData<PagingData<UserItem>> = currentQuery.switchMap { query ->
        useCase(query).cachedIn(viewModelScope)
            .asLiveData()
    }

    fun setSearchQuery(query: String) {
        if (currentQuery.value != query) {
            currentQuery.value = query
        }
    }
}