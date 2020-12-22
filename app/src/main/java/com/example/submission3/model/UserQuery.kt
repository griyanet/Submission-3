package com.example.submission3.model

data class UserQuery(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)