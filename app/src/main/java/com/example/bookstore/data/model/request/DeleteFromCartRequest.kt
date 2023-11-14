package com.example.bookstore.data.model.request

data class DeleteFromCartRequest(
    val userId: String? = null,
    val id: Int? = null
)