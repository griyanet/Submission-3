package com.example.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.example.submission3"
    const val SCHEME = "content"

    class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_table"
            const val _ID = "_id"
            const val AVATAR_URL = "avatar_url"
            const val FOLLOWER_URL = "follower_url"
            const val FOLLOWING_URL = "following_url"
            const val HTML_URL = "html_url"
            const val LOGIN = "login"
            const val REPOS_URL = "repos_url"
            const val URL = "url"

            val CONTEN_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}