package com.example.submission3.ui.userdetails

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.submission3.R
import com.example.submission3.adapter.ViewPagerAdapter
import com.example.submission3.databinding.FragmentUserDetailBinding
import com.example.submission3.repository.Repository
import com.example.submission3.ui.favorites.FavoritesViewModel
import com.example.submission3.viewmodel.MainViewModel
import com.example.submission3.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailFragment : Fragment() {

    private val args by navArgs<UserDetailFragmentArgs>()
    private lateinit var viewModel: MainViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private var savedFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val item = UserDetailFragmentArgs.fromBundle(arguments as Bundle).Item
        val username = item.login
        viewModel.getUserNameDetail(username)
        viewModel.getUserDetail()
        viewModel.userDetails.observe(viewLifecycleOwner, { response ->
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

        val fragmentList = arrayListOf(
            FollowerFragment(),
            FollowingFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList, requireActivity().supportFragmentManager, lifecycle
        )
        adapter.username = username
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Follower"
                1 -> tab.text = "Following"
            }
        }.attach()

        checkIsFavorite()
        Log.d("argsItemId", args.Item.login)
        Log.d("savedFavorite", savedFavorite.toString())

        binding.fabFavorite.setOnClickListener {
            if (savedFavorite) {
                removeFavorite()
            } else {
                saveToFavorites()
            }

            findNavController().navigate(R.id.action_userDetailFragment_to_navigation_favorites)
        }

        binding.fabFavorite.setImageDrawable(activity?.let { button ->
            getDrawable(
                button,
                R.drawable.ic_favorite_full
            )
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            removeFavorite()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkIsFavorite() {

    }

    private fun saveToFavorites() {
        val userFavorite = args.Item
        favoritesViewModel.addFavorite(userFavorite)
        showSnackBar("Congratulations! User (${args.Item.login}) added to Favorite")
        savedFavorite = true
    }

    private fun removeFavorite() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            val currentFav = args.Item
            favoritesViewModel.deleteFavorite(currentFav)
            savedFavorite = true
            Snackbar.make(
                binding.userDetailFragment,
                "Successfully deleted article",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Undo") {
                    favoritesViewModel.addFavorite(currentFav)
                }
                show()
            }
            findNavController().navigate(R.id.action_userDetailFragment_to_navigation_favorites)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete current Favorite")
        builder.setMessage("Are you sure want to delete (${args.Item.login})?")
        builder.create().show()

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.userDetailFragment,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

}
