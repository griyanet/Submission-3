package com.example.submission3.ui.favorites

import android.app.AlertDialog
import android.content.Context
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.R
import com.example.submission3.adapter.FavoriteAdapter
import com.example.submission3.database.DatabaseContract.FavoriteColumns.Companion.CONTEN_URI
import com.example.submission3.databinding.FavoritesFragmentBinding
import com.example.submission3.model.Item
import com.example.submission3.provider.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var listFavoritesUser: MutableList<Item> = mutableListOf()
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavorites()
            }
        }
        val iContext = requireContext()
        iContext.contentResolver.registerContentObserver(CONTEN_URI, true, myObserver)

        favoriteAdapter = FavoriteAdapter(listFavoritesUser)
        setHasOptionsMenu(true)
        setupRecyclerView()

        fadeIn()

        return binding.root
    }

    private fun queryAll(context: Context): LiveData<List<Item>> {
        val liveData = MutableLiveData<List<Item>>()
        val cursor = context.contentResolver.query(CONTEN_URI, null, null, null, null)
        cursor?.let {
            liveData.postValue(MappingHelper.mapCursorToArrayList(it))
            cursor.close()
        }
        return liveData
    }

    private fun loadFavorites() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBarFav.visibility = View.VISIBLE

            queryAll(context = requireContext()).observe(this@FavoritesFragment, {
                listFavoritesUser.addAll(it)
            })
        }
        binding.progressBarFav.visibility = View.INVISIBLE
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllFavoriteUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllFavoriteUsers() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            //todos
            Toast.makeText(
                requireContext(),
                "Successfully remove all Favorite Users!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete All Favorites")
        builder.create().show()
    }

    private fun setupRecyclerView() {
        binding.rvUserFav.setHasFixedSize(true)
        binding.rvUserFav.adapter = favoriteAdapter
        binding.rvUserFav.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fadeIn() {
        binding.vBlackScreenFav.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

}