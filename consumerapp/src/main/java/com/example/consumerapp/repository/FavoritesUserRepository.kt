package com.example.consumerapp.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.consumerapp.model.Item
import com.example.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTEN_URI

class FavoritesUserRepository() {

    fun selectAll(context: Context): LiveData<List<Item>> {
        val itemList = MutableLiveData<List<Item>>()

        val cursor = context.contentResolver.query(CONTEN_URI, null, null, null, null)
        cursor.let {
            itemList.postValue(it?.toListItem())
            cursor?.close()
        }
        return itemList
    }

    suspend fun insertFavorite(context: Context, item: Item) {
        val values = item.toContentValues()
        context.contentResolver.insert(CONTEN_URI, values)
    }

    suspend fun deleteFavorite(context: Context, uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }
}