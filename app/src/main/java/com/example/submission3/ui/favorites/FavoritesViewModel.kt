package com.example.submission3.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.submission3.database.FavoritesDatabase
import com.example.submission3.model.Item
import com.example.submission3.repository.FavoritesUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _readAllFavorites: LiveData<List<Item>>
    val readAllFavorites get() = _readAllFavorites
    private val repository: FavoritesUserRepository

    init {
        val favoritesDao = FavoritesDatabase.getDatabase(application).favoritesDao()
        repository = FavoritesUserRepository(favoritesDao)
        _readAllFavorites = repository.readAllData
    }

    fun addFavorite(favorite: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavorite(favorite)
        }
    }

    fun deleteFavorite(favorite: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(favorite)
        }
    }

    fun deleteAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllFavorites()
        }
    }

    fun isExist(id: Int): Boolean = repository.isExist(id)

}