package com.example.submission3.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.submission3.model.Item

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Item): Long

    @Delete
    suspend fun deleteFavorite(favorite: Item)

    @Query("DELETE FROM favorite_table WHERE id = :id")
    fun deleteById(id: Int): Int

    @Query("SELECT * FROM favorite_table ORDER BY id ASC")
    fun readAll(): LiveData<List<Item>>

    @Query("DELETE FROM favorite_table")
    suspend fun deleteAllFavorites()

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    fun selectBytId(id: Int): Cursor

    @Query("SELECT * FROM favorite_table")
    fun selectAll(): Cursor

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_table WHERE id = :id)")
    fun exists(id: Int): Boolean
}