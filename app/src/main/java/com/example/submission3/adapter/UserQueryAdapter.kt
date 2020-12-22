package com.example.submission3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission3.databinding.RowItemuserBinding
import com.example.submission3.model.Item

class UserQueryAdapter(
    private var list: List<Item>,
    private val listener: (Item) -> Unit
) : RecyclerView.Adapter<UserQueryAdapter.UserViewHolder>() {

    class UserViewHolder(private val binding: RowItemuserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun userBind(item: Item, listener: (Item) -> Unit) {
            binding.tvLogin.text = item.login
            binding.tvUserUrl.text = item.url
            binding.tvFollowerUrl.text = item.followers_url
            binding.tvFollowingUrl.text = item.following_url
            Glide.with(itemView)
                .load(item.avatar_url)
                .into(binding.ivAvatar)
            binding.cvCardView.setOnClickListener {
                listener(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemuserBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userBind(list[position], listener)
    }

    override fun getItemCount(): Int = list.size

}