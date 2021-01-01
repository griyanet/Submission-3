package com.example.submission3.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_table")
@Parcelize
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val avatar_url: String,
    val followers_url: String,
    val following_url: String,
    val html_url: String,
    var login: String,
    val repos_url: String,
    val url: String
) : Parcelable
