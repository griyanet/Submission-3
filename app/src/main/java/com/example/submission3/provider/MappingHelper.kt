package com.example.submission3.provider

import android.content.ContentValues
import android.database.Cursor
import com.example.submission3.database.DatabaseContract
import com.example.submission3.model.Item

object MappingHelper {

    private const val COLUMN_ID = "_id"
    private const val COLUMN_AVATAR_URL = "avatarUrl"
    private const val COLUMN_FOLLOWERS_URL = "followersUrl"
    private const val COLUMN_FOLLOWING_URL = "followingUrl"
    private const val COLUMN_HTML_URL = "htmlUrl"
    private const val COLUMN_LOGIN = "login"
    private const val COLUMN_REPOS_URL = "reposUrl"
    private const val COLUMN_URL = "url"

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Item> {
        val favoriteList = ArrayList<Item>()
        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                val followersUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWER_URL))
                val followingUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING_URL))
                val htmlUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.HTML_URL))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
                val reposUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOS_URL))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.URL))
                favoriteList.add(Item(id, avatarUrl, followersUrl, followingUrl, htmlUrl, login, reposUrl, url))
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(favoriteCursor: Cursor): Item {
        var item: Item
        favoriteCursor.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
            val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
            val followerUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWER_URL))
            val followingUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING_URL))
            val htmlUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.HTML_URL))
            val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
            val reposUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOS_URL))
            val url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.URL))
            item = Item(id, avatarUrl, followerUrl, followingUrl, htmlUrl, login, reposUrl, url)
        }
        return item
    }

    fun Item.toValues(): ContentValues =
        ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_AVATAR_URL, avatar_url)
            put(COLUMN_FOLLOWERS_URL, followers_url)
            put(COLUMN_FOLLOWING_URL, following_url)
            put(COLUMN_HTML_URL, html_url)
            put(COLUMN_LOGIN, login)
            put(COLUMN_REPOS_URL, repos_url)
            put(COLUMN_URL, url)
        }
}