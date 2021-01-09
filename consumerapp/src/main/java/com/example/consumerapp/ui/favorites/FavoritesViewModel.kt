package com.example.consumerapp.ui.favorites

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumerapp.model.Item
import com.example.consumerapp.repository.FavoritesUserRepository
import kotlinx.coroutines.launch

class FavoritesViewModel() : ViewModel() {

    private val repository: FavoritesUserRepository = FavoritesUserRepository()
    private var _readAllFavorites: MutableLiveData<List<Item>> = MutableLiveData()
    val readAllFavorites: LiveData<List<Item>>
    get() = _readAllFavorites

    fun selectAll(context: Context) {
        _readAllFavorites = repository.selectAll(context) as MutableLiveData<List<Item>>
    }

    fun insertFavorite(context: Context, item: Item) {
        viewModelScope.launch {
            repository.insertFavorite(context, item)
        }
    }

    fun deleteFavorite(context: Context, uri: Uri) {
        viewModelScope.launch {
            repository.deleteFavorite(context, uri)
        }
    }

}