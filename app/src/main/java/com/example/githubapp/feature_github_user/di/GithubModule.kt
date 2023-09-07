package com.example.githubapp.feature_github_user.di

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.githubapp.R
import com.example.githubapp.core.BASE_URL
import com.example.githubapp.feature_github_user.data.remote.RetrofitApi
import com.example.githubapp.feature_github_user.data.remote.repository.GithubRepositoryImpl
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import com.example.githubapp.feature_github_user.domain.repository.GithubRepository
import com.example.githubapp.feature_github_user.domain.usecase.GetFollowersUseCase
import com.example.githubapp.feature_github_user.domain.usecase.GetRepoUseCase
import com.example.githubapp.feature_github_user.domain.usecase.GetUserListUseCase
import com.example.githubapp.feature_github_user.presentation.ui.adapter.UserAdapter
import com.example.githubapp.feature_github_user.presentation.callback.OnUserItemClickListener
import com.example.githubapp.feature_github_user.presentation.ui.fragment.UserListingFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GithubModule {
    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide
        .with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )

    @Provides
    fun provideUserAdapter(glide: RequestManager): UserAdapter {
        return UserAdapter(glide)
    }

    @Provides
    @Singleton
    fun provideUserListUseCase(repository: GithubRepository) : GetUserListUseCase = GetUserListUseCase(repository)

    @Provides
    @Singleton
    fun provideRepoUseCase(repository: GithubRepository) : GetRepoUseCase = GetRepoUseCase(repository)

    @Provides
    @Singleton
    fun provideFollowerUseCase(repository: GithubRepository) : GetFollowersUseCase = GetFollowersUseCase(repository)

    @Provides
    @Singleton
    fun provideGithubRepositoryImpl(api :RetrofitApi): GithubRepository = GithubRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideRetrofitApi(client: OkHttpClient):RetrofitApi = Retrofit
        .Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitApi::class.java)

    @Singleton
    @Provides
    fun injectInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}