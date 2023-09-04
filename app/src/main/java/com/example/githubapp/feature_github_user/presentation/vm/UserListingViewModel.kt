package com.example.githubapp.feature_github_user.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubapp.core.Resource
import com.example.githubapp.core.UIState
import com.example.githubapp.feature_github_user.domain.model.followers.ResponseFollowersItem
import com.example.githubapp.feature_github_user.domain.model.repo.RepoItem
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import com.example.githubapp.feature_github_user.domain.usecase.GetFollowersUseCase
import com.example.githubapp.feature_github_user.domain.usecase.GetRepoUseCase
import com.example.githubapp.feature_github_user.domain.usecase.GetUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListingViewModel @Inject constructor(
    private val useCaseUserList: GetUserListUseCase,
    private val useCaseRepo: GetRepoUseCase,
    private val useCaseFollower: GetFollowersUseCase
) :ViewModel(){

    private val _uiStateUserList = MutableLiveData<UIState<PagingData<UserItem>>>()
    val uiStateUserList: LiveData<UIState<PagingData<UserItem>>> = _uiStateUserList

    private val _uiStateRepoList = MutableLiveData<UIState<List<RepoItem>>>()
    val uiStateRepoList: LiveData<UIState<List<RepoItem>>> = _uiStateRepoList

    private val _uiStateFollowerList = MutableLiveData<UIState<List<ResponseFollowersItem>>>()
    val uiStateFollowerList: LiveData<UIState<List<ResponseFollowersItem>>> = _uiStateFollowerList
    fun fetchPagingData(query: String) {
        if (query.isNotEmpty()) {
            // Show loading state
            _uiStateUserList.value = UIState.Loading(true)
            viewModelScope.launch {
                try {
                    val data = useCaseUserList(query).cachedIn(viewModelScope)
                    data.collect(){
                        _uiStateUserList.value = UIState.Loading(false)
                        _uiStateUserList.value = UIState.Success(it)
                    }
                } catch (e: Exception) {
                    _uiStateUserList.value = UIState.Loading(false)
                    _uiStateUserList.value = UIState.Error(e.localizedMessage ?: "An error occurred")
                }
            }
        }
    }

    fun fetchRepoData(url :String){
        _uiStateRepoList.value = UIState.Loading(true)
        viewModelScope.launch {
            try {
                val data = useCaseRepo(url)
                data.collect(){
                    _uiStateRepoList.value = UIState.Loading(false)
                    if(it is Resource.Success ){
                        _uiStateRepoList.value = it.data?.let { list->
                            if (list.isNotEmpty())
                                UIState.Success(list)
                            else
                                UIState.Error("Have no repository yet !")
                        }?:UIState.Error("No Data found")
                    }
                }

            }catch (e:Exception){
                _uiStateUserList.value = UIState.Loading(false)
                _uiStateRepoList.value = UIState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }


    fun fetchFollowersData(url :String){
        _uiStateFollowerList.value = UIState.Loading(true)
        viewModelScope.launch {
            try {
                val data = useCaseFollower(url)
                data.collect(){
                    _uiStateFollowerList.value = UIState.Loading(false)
                    if(it is Resource.Success ){
                        _uiStateFollowerList.value = it.data?.let {list->
                            if (list.isNotEmpty())
                                UIState.Success(list)
                            else
                                UIState.Error("No followers yet !")

                        }?:UIState.Error("No Data found")
                    }
                }
            }catch (e:Exception){
                _uiStateFollowerList.value = UIState.Loading(false)
                _uiStateFollowerList.value = UIState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

}