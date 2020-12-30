package com.example.submission3.repository

import androidx.lifecycle.LiveData
import com.example.submission3.database.FavoritesDao
import com.example.submission3.model.Item

class FavoritesUserRepository(private val favoritesDao: FavoritesDao) {

    val readAllData: LiveData<List<Item>> = favoritesDao.readAll()

    suspend fun addFavorite(favorite: Item) {
        favoritesDao.insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: Item) {
        favoritesDao.deleteFavorite(favorite)
    }

    suspend fun findByLogin(login: String): Item {
        return favoritesDao.findByLogin(login)
    }

    suspend fun deleteAllFavorites() {
        favoritesDao.deleteAllFavorites()
    }

}