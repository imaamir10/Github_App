package com.example.githubapp.feature_github_user.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.githubapp.databinding.ItemUserBinding
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import javax.inject.Inject

class UserAdapter @Inject constructor(private val glide : RequestManager): PagingDataAdapter<UserItem,RecyclerView.ViewHolder>(UserItemCallbackDiff()) {
    class UserItemCallbackDiff : DiffUtil.ItemCallback<UserItem>() {
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.id == newItem.id
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserViewHolder).bind(getItem(position) as UserItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    inner class UserViewHolder(private val mBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(userItem: UserItem) {
            glide.load(userItem.avatar_url).into(mBinding.ivUserImage)
            mBinding.tvUserName.text = userItem.login
        }
    }
}