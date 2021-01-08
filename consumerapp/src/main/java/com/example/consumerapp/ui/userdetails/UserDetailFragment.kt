package com.example.consumerapp.ui.userdetails

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.adapter.ViewPagerAdapter
import com.example.consumerapp.databinding.FragmentUserDetailBinding
import com.example.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTEN_URI
import com.example.consumerapp.db.MappingHelper
import com.example.consumerapp.db.MappingHelper.toValues
import com.example.consumerapp.model.Item
import com.example.consumerapp.repository.Repository
import com.example.consumerapp.viewmodel.MainViewModel
import com.example.consumerapp.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

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
            Log.i("savedInstanceState", savedInstanceState.toString())
            initFavorites()
        } else {
            savedInstanceState.getParcelableArrayList<Item>(EXTRA_STATE).also {
                val savedId = it?.first()?.login
                Log.i("savedId", savedId.toString())
                val argsId = args.Item.login
                Log.i("argsId", argsId.toString())
            }
        }

        binding.fabFavorite.setOnClickListener {
            if (savedFavorite) {
                removeFavorite()
            } else {
                saveToFavorites()
            }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    private fun initFavorites() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFav = async(Dispatchers.IO) {
                val currentFav = findFavId(args.Item.id)
                val uriItemId = Uri.parse("$CONTEN_URI/ITEM/$currentFav")
                val cursor = requireActivity().applicationContext.contentResolver.query(
                    uriItemId,
                    null,
                    null,
                    null,
                    null
                )
                Log.i("cursor", cursor.toString())
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val items = deferredFav.await()
            Log.i("items", items.toString())
            savedFavorite = items.size > 0
            if (savedFavorite) {
                binding.fabFavorite.setImageDrawable(activity?.let { button ->
                    getDrawable(
                        button,
                        R.drawable.ic_delete
                    )
                })
            } else {
                binding.fabFavorite.setImageDrawable(activity?.let { button ->
                    getDrawable(
                        button,
                        R.drawable.ic_favorite_full
                    )
                })
            }
        }
    }

    private fun saveToFavorites() {
        val userFavorite = args.Item
        requireActivity().applicationContext.contentResolver.insert(
            CONTEN_URI,
            userFavorite.toValues()
        )
        showSnackBar("Successfully added Favorite User to the list!")
        findNavController().navigate(R.id.action_userDetailFragment2_to_favoritesFragment)
    }

    private fun findFavId(itemId: Int): Int {
        val uriItemId = Uri.parse("$CONTEN_URI/ITEM/$itemId")
        val cursor = requireActivity().applicationContext.contentResolver.query(uriItemId, null, null, null, null)
        val favItemList = MappingHelper.mapCursorToArrayList(cursor)
        return if (favItemList.size > 0) {
            favItemList.first().id
        } else {
            0
        }
    }

    private fun removeFavorite() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            val currentFav = findFavId(args.Item.id)
            uriWithID = Uri.parse("$CONTEN_URI/$currentFav")
            val res = requireActivity().applicationContext.contentResolver.delete(uriWithID, currentFav.toString(), null)
            Snackbar.make(
                binding.userDetailFragment,
                "Successfully deleted article",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Undo") {
                    saveToFavorites()
                    findNavController().navigate(R.id.userDetailFragment2, arguments)
                }
                show()
            }
            lifecycleScope.launch(Dispatchers.Main) {
                delay(3000)
                findNavController().navigate(R.id.action_userDetailFragment2_to_favoritesFragment)
            }
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
