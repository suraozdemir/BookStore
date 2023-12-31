package com.example.bookstore

import android.content.Context

class StringResourceProvider(private val context: Context) {
    operator fun invoke(resourceId: Int): String {
        return context.getString(resourceId)
    }
}