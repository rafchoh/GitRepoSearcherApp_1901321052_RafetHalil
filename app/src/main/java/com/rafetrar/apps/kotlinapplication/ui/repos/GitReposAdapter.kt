package com.rafetrar.apps.kotlinapplication.ui.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rafetrar.apps.kotlinapplication.R
import com.rafetrar.apps.kotlinapplication.data.GitRepoItem
import com.rafetrar.apps.kotlinapplication.databinding.ItemGithubRepoBinding

class GitReposAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<GitRepoItem, GitReposAdapter.RepoViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GitReposAdapter.RepoViewHolder =
        RepoViewHolder(
            ItemGithubRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: GitReposAdapter.RepoViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) {

            holder.bind(currentItem)
        }
    }

    inner class RepoViewHolder(
        private val binding: ItemGithubRepoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {

                if (bindingAdapterPosition != RecyclerView.NO_POSITION){

                    val item = getItem(bindingAdapterPosition)

                    if(item != null){

                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(repo: GitRepoItem) {

            binding.apply {

                Glide.with(itemView)
                    .load(repo.owner.avatar_url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_android_black)
                    .into(ownerAvatar)

                textRepoName.text = repo.name
                textOwnerName.text = repo.owner.login
                textLanguage.text = "Language: " + repo.language
                textWatchersCount.text = "Watchers: " + repo.watchers_count.toString()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(repo: GitRepoItem)
    }

    companion object {

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<GitRepoItem>() {

            override fun areItemsTheSame(oldItem: GitRepoItem, newItem: GitRepoItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GitRepoItem, newItem: GitRepoItem): Boolean =
                oldItem == newItem
        }
    }
}