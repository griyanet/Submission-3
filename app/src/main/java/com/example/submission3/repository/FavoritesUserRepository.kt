package com.example.submission3.repository

import androidx.lifecycle.LiveData
import com.example.submission3.database.FavoritesDao
import com.example.submission3.model.Item

class FavoritesUserRepository(private val favoritesDao: FavoritesDao) {

    val readAllData: LiveData<List<Item>> = favoritesDao.readAll()

    fun addFavorite(favorite: Item) {
        favoritesDao.insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: Item) {
        favoritesDao.deleteFavorite(favorite)
    }

    fun isExist(id: Int) = favoritesDao.exists(id)

    suspend fun deleteAllFavorites() {
        favoritesDao.deleteAllFavorites()
    }

}