package com.example.bookstore.data.model.response

import com.example.bookstore.data.util.Product

data class GetProductDetailResponse(
    val message: String?,
    val product: Product,
    val status: Int?
)