package com.example.bookstore.data.model.request

data class AddToCartRequest(
    val userId: String,
    val productId: Int
)