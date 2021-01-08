package com.example.submission3.ui.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.R
import com.example.submission3.adapter.FavoriteAdapter
import com.example.submission3.database.DatabaseContract.FavoriteColumns.Companion.CONTEN_URI
import com.example.submission3.databinding.FavoritesFragmentBinding
import com.example.submission3.provider.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)



        adapter = FavoriteAdapter()
        setHasOptionsMenu(true)
        setupRecyclerView()

        fadeIn()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBarFav.visibility = View.VISIBLE
            val deferredFav = async(Dispatchers.IO) {
                val cursor = requireActivity().applicationContext.contentResolver.query(
                    CONTEN_URI,
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
            if (items.size > 0) {
                adapter.listFav = items
                showSnackBar("Congratulations!. There are ${adapter.listFav.size} users in the list")
                binding.progressBarFav.visibility = View.GONE
            } else {
                adapter.listFav = ArrayList()
                showSnackBar("There is no list of Favorite Users yet!")
                binding.progressBarFav.visibility = View.GONE
                binding.favoriteBlank.root.visibility = View.VISIBLE
            }
        }
        binding.progressBarFav.visibility = View.GONE
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
            requireActivity().applicationContext.contentResolver.delete(CONTEN_URI, null, null)
            Toast.makeText(
                requireContext(),
                "Successfully remove all Favorite Users!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.navigation_favorites)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete All Favorites")
        builder.create().show()
    }

    private fun setupRecyclerView() {
        binding.rvUserFav.setHasFixedSize(true)
        binding.rvUserFav.adapter = adapter
        binding.rvUserFav.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fadeIn() {
        binding.vBlackScreenFav.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.favoritesFragment,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

}