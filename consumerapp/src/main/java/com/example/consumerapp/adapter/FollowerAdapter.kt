package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.databinding.RowItemuserBinding
import com.example.consumerapp.model.FollowersItem

class FollowerAdapter : RecyclerView.Adapter<FollowerAdapter.MyViewHolder>() {

    private var userList = ArrayList<FollowersItem>()

    class MyViewHolder(private val binding: RowItemuserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun userBind(userItem: FollowersItem) {
            binding.tvLogin.text = userItem.login
            binding.tvUserUrl.text = userItem.url
            binding.tvFollowerUrl.text = userItem.followers_url
            binding.tvFollowingUrl.text = userItem.following_url
            Glide.with(itemView)
                .load(userItem.avatar_url)
                .into(binding.ivAvatar)
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemuserBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.userBind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun setData(newList: ArrayList<FollowersItem>) {
        userList.clear()
        userList = newList
        notifyDataSetChanged()
    }

}