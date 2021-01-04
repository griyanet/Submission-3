package com.example.submission3.ui.userdetails

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
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
import com.example.submission3.database.DatabaseContract.FavoriteColumns.Companion.CONTEN_URI
import com.example.submission3.databinding.FragmentUserDetailBinding
import com.example.submission3.model.Item
import com.example.submission3.provider.MappingHelper
import com.example.submission3.provider.MappingHelper.toValues
import com.example.submission3.repository.Repository
import com.example.submission3.viewmodel.MainViewModel
import com.example.submission3.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetailFragment : Fragment() {

    private val args by navArgs<UserDetailFragmentArgs>()
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!
    private var savedFavorite = false
    private lateinit var uriWithID: Uri
    private var listFav = ArrayList<Item>()

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)

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

        if (savedInstanceState == null) {
            initFavorites()
        } else {
            savedInstanceState.getParcelableArrayList<Item>(EXTRA_STATE).also {
                val savedId = it?.first()?.id
                val argsId = args.Item.id
                if (savedId == argsId) {
                    savedFavorite = true
                    binding.fabFavorite.setImageDrawable(activity?.let { button ->
                        getDrawable(
                            button,
                            R.drawable.ic_delete
                        )
                    })
                } else {
                    savedFavorite = false
                    binding.fabFavorite.setImageDrawable(activity?.let { button ->
                        getDrawable(
                            button,
                            R.drawable.ic_favorite_full
                        )
                    })
                }
            }
        }
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

    private fun initFavorites() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFav = async(Dispatchers.IO) {
                val iContext = requireContext()
                val cursor = iContext.contentResolver.query(CONTEN_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val items = deferredFav.await()
            if (items.size > 0) {
                listFav = items
            } else {
                listFav = ArrayList()
                Log.d("lisFav", listFav.toString())
            }
        }
    }

    private fun insertValues(item: Item, context: Context) {
        context.contentResolver.insert(CONTEN_URI, item.toValues())
    }
    private fun saveToFavorites() {
        val userFavorite = args.Item
        insertValues(userFavorite, context = requireContext())
    }

    private fun removeFavorite() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            val currentFav = args.Item.id
            uriWithID = Uri.parse("$CONTEN_URI/$currentFav")
            val iContext = requireContext()
            iContext.contentResolver.delete(CONTEN_URI, uriWithID.toString(), null)
            Snackbar.make(
                binding.userDetailFragment,
                "Successfully deleted article",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Undo") {
                    saveToFavorites()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listFav)
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
