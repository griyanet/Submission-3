package com.example.consumerapp.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.consumerapp.model.Item

const val COLUMN_ID = "id"
const val COLUMN_AVATAR_URL = "avatar_url"
const val COLUMN_FOLLOWERS_URL = "followers_url"
const val COLUMN_FOLLOWING_URL = "following_url"
const val COLUMN_HTML_URL = "html_url"
const val COLUMN_LOGIN = "login"
const val COLUMN_REPOS_URL = "repos_url"
const val COLUMN_URL = "url"

fun ContentValues.toItem(): Item =
    Item(
        id = getAsInteger(COLUMN_ID),
        avatar_url = getAsString(COLUMN_AVATAR_URL),
        followers_url = getAsString(COLUMN_FOLLOWERS_URL),
        following_url = getAsString(COLUMN_FOLLOWING_URL),
        html_url = getAsString(COLUMN_HTML_URL),
        login = getAsString(COLUMN_LOGIN),
        repos_url = getAsString(COLUMN_REPOS_URL),
        url = getAsString(COLUMN_URL)
    )

fun Item.toContentValues(): ContentValues =
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

fun Cursor.toListItem(): ArrayList<Item> {
    val listItem = ArrayList<Item>()
    apply {
        while (moveToNext()) {
            listItem.add(this.toItem())
        }
    }
    return listItem
}

fun Cursor.toItem(): Item =
    Item(
        getInt(getColumnIndexOrThrow(COLUMN_ID)),
        getString(getColumnIndexOrThrow(COLUMN_AVATAR_URL)),
        getString(getColumnIndexOrThrow(COLUMN_FOLLOWERS_URL)),
        getString(getColumnIndexOrThrow(COLUMN_FOLLOWING_URL)),
        getString(getColumnIndexOrThrow(COLUMN_HTML_URL)),
        getString(getColumnIndexOrThrow(COLUMN_LOGIN)),
        getString(getColumnIndexOrThrow(COLUMN_REPOS_URL)),
        getString(getColumnIndexOrThrow(COLUMN_URL))
    )