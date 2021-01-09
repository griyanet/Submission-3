package com.example.submission3.ui.userdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.viewmodel.MainViewModel
import com.example.submission3.viewmodel.MainViewModelFactory
import com.example.submission3.repository.Repository
import com.example.submission3.adapter.FollowerAdapter
import com.example.submission3.databinding.FragmentFollowerBinding


class FollowerFragment : Fragment() {

    companion object {
        const val ARG_USERNAME = "username"
        fun newInstance(username: String) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }

    private lateinit var viewModel: MainViewModel
    private val userAdapter by lazy { FollowerAdapter() }
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        val username = arguments?.getString(ARG_USERNAME)
        username?.let { Log.d("ARG_USERNAME", it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        username?.let { Log.d("ARG_USERNAME", it) }

        binding.rvUserFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvUserFollower.setHasFixedSize(true)
        binding.rvUserFollower.adapter = userAdapter

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (username != null) {
            viewModel.getUserFollower(username)
        }
        viewModel.userFollower.observe(viewLifecycleOwner, { response ->
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
        }?.start()
    }

}