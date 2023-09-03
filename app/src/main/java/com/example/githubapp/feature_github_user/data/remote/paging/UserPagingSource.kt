package com.example.githubapp.feature_github_user.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubapp.feature_github_user.data.remote.RetrofitApi
import com.example.githubapp.feature_github_user.domain.model.user.UserItem

class UserPagingSource(private val api: RetrofitApi,private val query: String) : PagingSource<Int, UserItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserItem> {
        try {
            // Load data from the API based on the params (page number, etc.)
            val page = params.key ?: 1 // Default to page 1 if key is null
            val response = api.getGetUser(page, params.loadSize,query)
            
            if (response.isSuccessful) {
                val data = response.body()?.items ?: emptyList()
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (data.isEmpty()) null else page + 1
                
                return LoadResult.Page(data, prevKey, nextKey)
            } else {
                return LoadResult.Error(Exception("API request failed"))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserItem>): Int? {
//        // If there are no items in the PagingState, return null
//        if (state.isEmpty()) {
//            return null
//        }
//
//        // Get the key of the item at the start of the list (the first page)
//        val firstItem = state.firstItemOrNull()
//        return firstItem?. ?: 1

        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}