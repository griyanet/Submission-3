package com.example.consumerapp.ui.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.adapter.FavoriteAdapter
import com.example.consumerapp.databinding.FavoritesFragmentBinding
import com.example.consumerapp.model.Item

class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private var listFavoritesUser: MutableList<Item> = mutableListOf()
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        viewModel.readAllFavorites.observe(viewLifecycleOwner, {
            if (it != null) {
                listFavoritesUser.clear()
                listFavoritesUser.addAll(it)
                favoriteAdapter.notifyDataSetChanged()
            }
        })

        favoriteAdapter = FavoriteAdapter(listFavoritesUser)
        setHasOptionsMenu(true)
        setupRecyclerView()

        fadeIn()

        return binding.root
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
            //viewModel.deleteAllFavorites()
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