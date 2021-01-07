package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.databinding.RowItemfavoritesBinding
import com.example.consumerapp.model.Item
import com.example.consumerapp.ui.favorites.FavoritesFragmentDirections

class FavoriteAdapter() :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    var listFav = ArrayList<Item>()
        set(lisFav) {
            this.listFav.clear()
            this.listFav.addAll(lisFav)
            notifyDataSetChanged()
        }

    class MyViewHolder(val binding: RowItemfavoritesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesUser: Item) {
            binding.tvLoginFav.text = favoritesUser.login
            binding.tvUserUrlFav.text = favoritesUser.url
            binding.tvFollowerUrl.text = favoritesUser.followers_url
            binding.tvFollowingUrl.text = favoritesUser.following_url
            Glide.with(itemView)
                .load(favoritesUser.avatar_url)
                .into(binding.ivAvatarFav)
            binding.cvFavorite.setOnClickListener {
                val action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToUserDetailFragment2(
                        favoritesUser
                    )
                itemView.findNavController().navigate(action)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemfavoritesBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = listFav.size

}