package com.example.bookstore.data.source.remote

import com.example.bookstore.common.Constants.EndPoints.ADD_TO_CART
import com.example.bookstore.common.Constants.EndPoints.CLEAR_CART
import com.example.bookstore.common.Constants.EndPoints.DELETE_FROM_CART
import com.example.bookstore.common.Constants.EndPoints.GET_CART_PRODUCTS
import com.example.bookstore.common.Constants.EndPoints.GET_PRODUCTS
import com.example.bookstore.common.Constants.EndPoints.GET_PRODUCT_DETAIL
import com.example.bookstore.common.Constants.EndPoints.GET_SALE_PRODUCTS
import com.example.bookstore.common.Constants.EndPoints.SEARCH_PRODUCT
import com.example.bookstore.data.model.request.AddToCartRequest
import com.example.bookstore.data.model.request.ClearCartRequest
import com.example.bookstore.data.model.request.DeleteFromCartRequest
import com.example.bookstore.data.model.response.BaseResponse
import com.example.bookstore.data.model.response.GetProductDetailResponse
import com.example.bookstore.data.model.response.GetResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @Headers("store: bookstore")
    @GET(GET_PRODUCTS)
    suspend fun getProducts(): GetResponse

    @Headers("store: bookstore")
    @GET(GET_SALE_PRODUCTS)
    suspend fun getSaleProducts() : GetResponse

    @Headers("store: bookstore")
    @GET(GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Query("id") id: Int
    ) : GetProductDetailResponse

    @Headers("store: bookstore")
    @GET(SEARCH_PRODUCT)
    suspend fun searchProduct(
        @Query("query") query: String?
    ) : GetResponse

    @Headers("store: bookstore")
    @POST(ADD_TO_CART)
    suspend fun addToCart(
        @Body addToCartRequest: AddToCartRequest
    ) : BaseResponse

    @Headers("store: bookstore")
    @GET(GET_CART_PRODUCTS)
    suspend fun getCartProducts(
        @Query("userId") userId : String?
    ) : GetResponse

    @Headers("store: bookstore")
    @POST(DELETE_FROM_CART)
    suspend fun deleteFromCart (
        @Body deleteFromCartRequest: DeleteFromCartRequest
    ) : BaseResponse

    @Headers("store: bookstore")
    @POST(CLEAR_CART)
    suspend fun clearCart(
        @Body clearCartRequest: ClearCartRequest
    ) : BaseResponse
}