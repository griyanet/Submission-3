package com.example.submission3.ui.favorites

import android.view.View

object viewUtil {
    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }
}