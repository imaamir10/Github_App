package com.example.githubapp.feature_github_user.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.example.githubapp.R
import com.example.githubapp.core.UIState
import com.example.githubapp.core.hideKeyboard
import com.example.githubapp.databinding.FragmentListingBinding
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import com.example.githubapp.feature_github_user.presentation.adapter.UserAdapter
import com.example.githubapp.feature_github_user.presentation.callback.OnUserItemClickListener
import com.example.githubapp.feature_github_user.presentation.vm.UserListingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class UserListingFragment : Fragment(R.layout.fragment_listing), OnUserItemClickListener {



    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var userAdapter: UserAdapter

    private val listingViewModel: UserListingViewModel by viewModels()
    private var fragmentBinding: FragmentListingBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentListingBinding.bind(view)
        fragmentBinding = binding
        setAdapter()
        setFetchUserDataObserver()
        setFollowersDataObserver()
        setRepoDataObserver()
        onQueryEntered()

//        listingViewModel.fetchFollowersData("https://api.github.com/users/aamirWasi/followers")

    }

    private fun setAdapter() {
        userAdapter.setOnItemClickListener(this)
        fragmentBinding?.rvUser?.adapter = userAdapter
    }

    private fun setFetchUserDataObserver() {
        listingViewModel.uiStateUserList.observe(viewLifecycleOwner){ state->
            when (state) {
                is UIState.Loading -> {
                    // Show loading indicator
                    Log.e("state", "Loading = ${state.isLoading}")
                    fragmentBinding?.progressBar?.visibility = if(state.isLoading) View.VISIBLE else View.INVISIBLE
                }
                is UIState.Success -> {
                    // Update the RecyclerView with the loaded data
                    userAdapter.submitData(lifecycle, state.data)

                }
                is UIState.Error -> {
                    // Show error message
                    Toast.makeText(context,state.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setRepoDataObserver() {
        listingViewModel.uiStateRepoList.observe(viewLifecycleOwner){ state->
            when(state){
                is UIState.Loading->{
                    fragmentBinding?.tvRepos?.text = "Getting Data..."
                }
                is UIState.Success->{
                    fragmentBinding?.tvRepos?.text = state.data.size.toString()
                }
                is UIState.Error -> {
                    Toast.makeText(context,state.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setFollowersDataObserver() {
        listingViewModel.uiStateFollowerList.observe(viewLifecycleOwner){ state->
            when(state){
                is UIState.Loading->{
                    fragmentBinding?.tvFollower?.text = "Getting Data..."
                }
                is UIState.Success->{
                    fragmentBinding?.tvFollower?.text = state.data.size.toString()
                }
                is UIState.Error -> {
                    Toast.makeText(context,state.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onQueryEntered() {
        fragmentBinding?.etSearch?.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (fragmentBinding?.etSearch?.text.toString().isNotEmpty()) {
                    listingViewModel.fetchPagingData(fragmentBinding?.etSearch?.text.toString())
                }
                activity?.let { hideKeyboard(it) }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onUserItemClicked(userItem: UserItem) {
        fragmentBinding?.tvUserName?.text = userItem.login
        fragmentBinding?.ivUserImage?.let {
            glide.load(userItem.avatar_url).into(it)
        }
        userItem.followers_url?.let { listingViewModel.fetchFollowersData(it) }
        userItem.repos_url?.let { listingViewModel.fetchRepoData(it) }

    }



}