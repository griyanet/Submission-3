package com.example.consumerapp.ui.userdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.FollowerAdapter
import com.example.consumerapp.databinding.FragmentFollowingBinding
import com.example.consumerapp.repository.Repository
import com.example.consumerapp.viewmodel.MainViewModel
import com.example.consumerapp.viewmodel.MainViewModelFactory

class FollowingFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME = "username"
        fun newInstance(username: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }

    private lateinit var viewModel: MainViewModel
    private val userAdapter by lazy { FollowerAdapter() }
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(FollowerFragment.ARG_USERNAME)
        username?.let { Log.d("ARG_USERNAME", it) }

        binding.rvUserFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvUserFollowing.setHasFixedSize(true)
        binding.rvUserFollowing.adapter = userAdapter

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (username != null) {
            viewModel.getUserFollowing(username)
        }
        viewModel.userFollowing.observe(viewLifecycleOwner, { response ->
            response.body().let {
                if (it != null) {
                    userAdapter.setData(it)
                }
            }
        })

        fadeIn()
        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun fadeIn() {
        binding.vBlackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }
}