package com.example.submission3.cp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.submission3.database.FavoritesDatabase

class FavoriteContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.example.submission3.cp.FavoriteContentProvider"
        private const val FAVORITE_TABLE = ".model.Item"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$FAVORITE_TABLE")
        private const val FAVORITES = 1
        private const val FAVORITE_ID = 2
        private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)
        var TAG = FavoriteContentProvider::class.java.simpleName

        init {
            sURIMatcher.addURI(AUTHORITY, FAVORITE_TABLE, FAVORITES)
            sURIMatcher.addURI(AUTHORITY, "$FAVORITE_TABLE/#", FAVORITE_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val favDao = context?.let { FavoritesDatabase.getDatabase(it).favoritesDao() }
        val count: Int = when (sURIMatcher.match(uri)) {
            FAVORITE_ID -> {
                uri.lastPathSegment?.toInt()?.let {
                    favDao?.deleteById(it)
                } ?: 0
            }
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val favDao = context?.let { FavoritesDatabase.getDatabase(it).favoritesDao() }
        val id = when (sURIMatcher.match(uri)) {
            FAVORITES -> {
                values.let {
                    it?.let { it1 -> favDao?.insertFavorite(it1.toItem()) }
                } ?: 0
            }
            else -> throw IllegalArgumentException("Unknown uri $uri")
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("CONTENT_URI/$id")
    }


    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val favDao = context?.let { FavoritesDatabase.getDatabase(it).favoritesDao() }
        return when (sURIMatcher.match(uri)) {
            FAVORITES -> favDao?.selectAll()
            FAVORITE_ID -> uri.lastPathSegment?.toInt()?.let { favDao?.selectBytId(it) }
            else -> throw IllegalArgumentException("Unknown uri $uri")
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}