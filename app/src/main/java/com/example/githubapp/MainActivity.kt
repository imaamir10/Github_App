package com.example.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubapp.databinding.ActivityMainBinding
import com.example.githubapp.feature_github_user.presentation.fragment.GithubFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}