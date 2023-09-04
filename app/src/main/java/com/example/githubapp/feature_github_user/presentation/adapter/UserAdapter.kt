package com.example.githubapp.feature_github_user.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.githubapp.databinding.ItemUserBinding
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import com.example.githubapp.feature_github_user.presentation.callback.OnUserItemClickListener
import javax.inject.Inject

class UserAdapter @Inject constructor(
    private val glide : RequestManager
): PagingDataAdapter<UserItem,RecyclerView.ViewHolder>(UserItemCallbackDiff()) {

    // Click listener property
    private var onItemClickListener: OnUserItemClickListener? = null

    // Set the click listener
    fun setOnItemClickListener(listener: OnUserItemClickListener) {
        this.onItemClickListener = listener
    }

    class UserItemCallbackDiff : DiffUtil.ItemCallback<UserItem>() {
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem == newItem
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
        init {
            mBinding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { item ->
                    onItemClickListener?.onUserItemClicked(item)
                }
            }
        }
        fun bind(userItem: UserItem) {
            glide.load(userItem.avatar_url).into(mBinding.ivUserImage)
            mBinding.tvUserName.text = userItem.login
        }
    }
}

