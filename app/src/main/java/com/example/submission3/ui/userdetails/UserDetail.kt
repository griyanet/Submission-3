package com.example.submission3.ui.userdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submission3.MainViewModel
import com.example.submission3.MainViewModelFactory
import com.example.submission3.R
import com.example.submission3.Repository
import com.example.submission3.adapter.SectionsPageAdapter
import com.example.submission3.databinding.ActivityUserDetailBinding
import com.example.submission3.model.Item

class UserDetail : AppCompatActivity() {

    companion object {
        const val USERNAME = "username"
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val item = intent.getParcelableExtra<Item>(USERNAME)
        val username = item?.login

        val adapter = SectionsPageAdapter(this, supportFragmentManager)
        adapter.username = username
        binding.viewPager.adapter = adapter
        adapter.addFragment(FollowerFragment(), resources.getString(R.string.follower))
        adapter.addFragment(FollowingFragment(), resources.getString(R.string.following))
        binding.tabs.setupWithViewPager(binding.viewPager)
        adapter.notifyDataSetChanged()

        supportActionBar?.elevation = 0f

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (username != null) {
            viewModel.getUserDetail(username)
        }
        viewModel.userDetails.observe(this, { response ->
            response.body()?.let {
                Glide.with(this)
                    .load(it.avatarUrl)
                    .into(binding.imgAvatar)
                binding.tvLoginId.text = it.login
                binding.tvName.text = it.name
                binding.tvLocation.text = it.location
                binding.tvFollower.text = it.followers.toString()
                binding.tvFollowing.text = it.following.toString()
                binding.tvCompany.text = it.company
            }
        })
    }


}