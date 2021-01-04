package com.example.consumerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Item(
    var id: Int,
    val avatar_url: String,
    val followers_url: String,
    val following_url: String,
    val html_url: String,
    var login: String,
    val repos_url: String,
    val url: String
) : Parcelable
