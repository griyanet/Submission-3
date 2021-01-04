package com.example.submission3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.submission3.database.DatabaseContract.AUTHORITY
import com.example.submission3.database.DatabaseContract.FavoriteColumns.Companion.CONTEN_URI
import com.example.submission3.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.example.submission3.database.FavoriteHelper

class FavoriteContentProvider : ContentProvider() {

    companion object {
        private const val FAVORITES = 1
        private const val FAVORITE_ID = 2
        private lateinit var favHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITES)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavoriteHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITES -> favHelper.queryAll()
            FAVORITE_ID -> favHelper.queryByID(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val added: Long? = when (FAVORITES) {
            sUriMatcher.match(uri) -> values?.let { favHelper.insert(it) }
            else -> 0
        }
        val iContext = requireContext()
        iContext.contentResolver.notifyChange(CONTEN_URI, null)
        return Uri.parse("$CONTEN_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favHelper.deleteByID(uri.lastPathSegment.toString())
            else -> 0
        }
        val iContext = requireContext()
        iContext.contentResolver.notifyChange(CONTEN_URI, null)
        return deleted
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val updated: Int? = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> values?.let {
                favHelper.update(uri.lastPathSegment.toString(),
                    it
                )
            }
            else -> 0
        }
        val iContext = requireContext()
        iContext.contentResolver.notifyChange(CONTEN_URI, null)
        return updated!!
    }
}