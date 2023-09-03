package com.example.githubapp.feature_github_user.presentation.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

class GithubFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            ListingFragment::class.java.name->ListingFragment()
            else->super.instantiate(classLoader, className)
        }
    }
}