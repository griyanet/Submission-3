package com.example.submission3.ui.favorites

import android.app.Application
import androidx.lifecycle.*
import com.example.submission3.database.FavoritesDatabase
import com.example.submission3.model.Item
import com.example.submission3.repository.FavoritesUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _readAllFavorites: LiveData<List<Item>>
    val readAllFavorites get() = _readAllFavorites
    private val repository: FavoritesUserRepository
    private val _favLogin: MutableLiveData<Item> = MutableLiveData()
    val favLogin: LiveData<Item>
    get() = _favLogin
    val loading: MutableLiveData<Boolean> = MutableLiveData()

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

    fun findByLogin(login: String): LiveData<Item> {
        viewModelScope.launch(Dispatchers.IO) {
            repository.findByLogin(login)
        }
        return _favLogin
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

}