package com.example.githubapp.feature_github_user.presentation.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentListingBinding
import com.example.githubapp.feature_github_user.presentation.adapter.UserAdapter
import com.example.githubapp.feature_github_user.presentation.vm.ListingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ListingFragment : Fragment(R.layout.fragment_listing) {

    private val listingViewModel : ListingViewModel by viewModels()
    private var fragmentBinding : FragmentListingBinding? = null

    @Inject
    lateinit var glide: RequestManager

    private val userAdatper: UserAdapter by lazy {
        UserAdapter(glide)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentListingBinding.bind(view)
        fragmentBinding = binding
        setAdapter()

        listingViewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
            userAdatper.submitData(lifecycle, pagingData)
        }

        binding.etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (binding.etSearch.text.toString().isNotEmpty()) {
                    listingViewModel.setSearchQuery(binding.etSearch.text.toString())
                }
                activity?.let { hideKeyboard(it) }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

            }

    private fun setAdapter() {
        fragmentBinding?.rvUser?.adapter =userAdatper
    }

    fun hideKeyboard(activity: FragmentActivity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}