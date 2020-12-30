package com.example.submission3.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.submission3.model.Item

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Item)

    @Delete
    suspend fun deleteFavorite(favorite: Item)

    @Query("SELECT * FROM favorite_table ORDER BY id ASC")
    fun readAll(): LiveData<List<Item>>

    @Query("DELETE FROM favorite_table")
    suspend fun deleteAllFavorites()

    @Query("SELECT * FROM favorite_table WHERE login=:login")
    suspend fun findByLogin(login: String): Item
}