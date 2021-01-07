package com.example.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.example.submission3"
    const val SCHEME = "content"

    class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favoriteUsers"
            const val _ID = "_id"
            const val ITEM_ID = "itemId"
            const val AVATAR_URL = "avatarUrl"
            const val FOLLOWER_URL = "followersUrl"
            const val FOLLOWING_URL = "followingUrl"
            const val HTML_URL = "htmlUrl"
            const val LOGIN = "login"
            const val REPOS_URL = "reposUrl"
            const val URL = "url"

            val CONTEN_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}