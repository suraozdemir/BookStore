package com.example.bookstore.data.model.response

import com.example.bookstore.data.util.Product

data class GetResponse (
    val message: String?,
    val products: List<Product>?,
    val status: Int?
)
