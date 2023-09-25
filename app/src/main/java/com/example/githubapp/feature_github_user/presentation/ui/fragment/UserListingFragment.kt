package com.example.githubapp.feature_github_user.presentation.ui.fragment

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
import com.example.githubapp.feature_github_user.presentation.ui.adapter.UserAdapter
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
        onQueryEntered()


    }

    private fun setAdapter() {
        userAdapter.setOnItemClickListener(this)
        fragmentBinding?.rvUser?.adapter = userAdapter
    }

    private fun setFetchUserDataObserver() {
        listingViewModel.combinedUserData.observe(viewLifecycleOwner) { combinedUserData ->
            when (combinedUserData.userState) {
                is UIState.Loading -> {
                    fragmentBinding?.progressBar?.visibility = if (combinedUserData.userState.isLoading) View.VISIBLE else View.INVISIBLE
                }
                is UIState.Success -> {
                    userAdapter.submitData(lifecycle, combinedUserData.userState.data)
                }
                is UIState.Error -> {
                    Toast.makeText(context,combinedUserData.userState.message,Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }

            when (combinedUserData.repoState) {
                is UIState.Loading -> {
                    fragmentBinding?.tvRepos?.text =  "Getting Data..."
                }
                is UIState.Success -> {
                    Log.e("repoData",combinedUserData.repoState.data.size.toString())
                    fragmentBinding?.tvRepos?.text = combinedUserData.repoState.data.size.toString()
                }
                is UIState.Error -> {
                    fragmentBinding?.tvRepos?.text = combinedUserData.repoState.message
                }
                else -> {

                }
            }

            when (combinedUserData.followerState) {
                is UIState.Loading -> {
                    fragmentBinding?.tvFollower?.text =  "Getting Data..."
                }
                is UIState.Success -> {
                    fragmentBinding?.tvFollower?.text = combinedUserData.followerState.data.size.toString()
                }
                is UIState.Error -> {
                    fragmentBinding?.tvFollower?.text = combinedUserData.followerState.message
                }
                else -> {

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


    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    /*
    * Memory Leak: Address the issue of fragmentBinding not being cleared to prevent potential memory leaks.
    * to solve this
    * this code is added
    * override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
    * */




}