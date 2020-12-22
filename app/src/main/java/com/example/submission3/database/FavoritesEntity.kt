package com.example.submission3.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
class FavoritesEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var name: String,
    var login: String,
    var avatarUrl: String,
    var company: String,
    var location: String,
    var url: String,
    var favoriteStatus: Boolean
)