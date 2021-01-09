package com.example.consumerapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.adapter.UserQueryAdapter
import com.example.consumerapp.databinding.FragmentHomeBinding
import com.example.consumerapp.model.Item
import com.example.consumerapp.repository.Repository
import com.example.consumerapp.viewmodel.MainViewModel
import com.example.consumerapp.viewmodel.MainViewModelFactory

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainViewModel
    private var listUserQuery: MutableList<Item> = mutableListOf()
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserQueryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        userAdapter = UserQueryAdapter(listUserQuery)

        setHasOptionsMenu(true)
        setupRecyclerView()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.searchMenu)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = resources.getString(R.string.search_hint)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingMenu -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {

            viewModel.getQueryUser(query)
            viewModel.getUserQuery()

            fadeIn()
            viewModel.loading.observe(viewLifecycleOwner, {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            })
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserQuery()
        viewModel.userQuery.observe(viewLifecycleOwner, { response ->
            response.body()?.items.let {
                if (it != null) {
                    listUserQuery.clear()
                    listUserQuery.addAll(it)
                    userAdapter.notifyDataSetChanged()
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

    private fun setupRecyclerView() {
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = userAdapter
        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fadeIn() {
        binding.vBlackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

}
