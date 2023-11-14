package com.example.bookstore.data.repo

import com.example.bookstore.common.Resource
import com.example.bookstore.data.mapper.mapToProductEntity
import com.example.bookstore.data.mapper.mapToProductUI
import com.example.bookstore.data.model.request.AddToCartRequest
import com.example.bookstore.data.model.request.ClearCartRequest
import com.example.bookstore.data.model.request.DeleteFromCartRequest
import com.example.bookstore.data.model.response.BaseResponse
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.data.source.local.ProductDao
import com.example.bookstore.data.source.remote.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productService: ProductService,
    private val productDao: ProductDao
) {

    suspend fun getProducts(): Resource<List<ProductUI>> {
        return withContext(Dispatchers.IO) {
            try {
                val favoriteList = productDao.getProductId()
                val result = productService.getProducts().products.orEmpty()
                if (result.isEmpty()) {
                    Resource.Error(Exception("Movies are not found!"))
                } else {
                    Resource.Success(result.map {
                        it.mapToProductUI(isFavorite = favoriteList.contains(it.id))
                    })
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }


    suspend fun getSaleProducts(): Resource<List<ProductUI>> {
        return withContext(Dispatchers.IO)
        {
            try {
                val favoriteList = productDao.getProductId()
                val result = productService.getSaleProducts().products.orEmpty()

                if (result.isEmpty()) {
                    Resource.Error(Exception("Sale books are not found!"))
                } else {
                    Resource.Success(result.map {
                        it.mapToProductUI(isFavorite = favoriteList.contains(it.id))
                    })
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    suspend fun getProductDetail(id: Int): Resource<ProductUI> {
        return withContext(Dispatchers.IO)
        {
            try {

                val favoriteNamesList = productDao.getProductId()
                val result = productService.getProductDetail(id).product
                result.let {
                    Resource.Success(
                        it.mapToProductUI(
                            isFavorite = favoriteNamesList.contains(
                                it.id
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    suspend fun searchProduct(query: String?): Resource<List<ProductUI>> {
        return withContext(Dispatchers.IO)
        {
            try {
                val favoriteList = productDao.getProductId()
                val result = productService.searchProduct(query).products
                result?.let {
                    Resource.Success(result.map {
                        it.mapToProductUI(isFavorite = favoriteList.contains(it.id))
                    })
                } ?: kotlin.run {
                    Resource.Error(Exception("There is no such a book!"))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    suspend fun addToCart(addToCartRequest: AddToCartRequest): Resource<BaseResponse> {
        return withContext(Dispatchers.IO)
        {
            try {
                val result = productService.addToCart(addToCartRequest)

                if (result.status == 200) {
                    Resource.Success(result)
                } else {
                    Resource.Error(Exception(result.message.toString()))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    suspend fun deleteFromCart(userId: String, id: Int): Resource<BaseResponse> {
        return withContext(Dispatchers.IO)
        {
            try {
                val result = productService.deleteFromCart(DeleteFromCartRequest(userId, id))

                if (result.status == 200) {
                    Resource.Success(result)
                } else {
                    Resource.Error(Exception(result.message.toString()))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    suspend fun clearCart(clearCartRequest: ClearCartRequest): Resource<BaseResponse> {
        return withContext(Dispatchers.IO)
        {
            try {
                val result = productService.clearCart(clearCartRequest)

                if (result.status == 200) {
                    Resource.Success(result)
                } else {
                    Resource.Error(Exception(result.message.toString()))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    suspend fun getCartProducts(userId: String?): Resource<List<ProductUI>> {
        return withContext(Dispatchers.IO)
        {
            try {
                val favoriteList = productDao.getProductId()
                val result = productService.getCartProducts(userId)

                if (result.status == 200) {
                    Resource.Success(result.products.orEmpty().map {
                        it.mapToProductUI(isFavorite = favoriteList.contains(it.id))
                    })
                } else {
                    Resource.Error(Exception(result.message.orEmpty()))
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    suspend fun addToFavorites(product: ProductUI) {
        productDao.addToFavorite(product.mapToProductEntity())
    }

    suspend fun deleteFromFavorites(product: ProductUI) {
        productDao.deleteFromFavorites(product.mapToProductEntity())
    }

    suspend fun clearFavorites() {
        productDao.clearFavorites()
    }


    suspend fun getFavoriteProducts(): Resource<List<ProductUI>> {
        return withContext(Dispatchers.IO)
        {
            try {
                val result = productDao.getProducts().map { it.mapToProductUI() }

                if (result.isEmpty()) {
                    Resource.Error(Exception("There are no products here!"))
                } else {
                    Resource.Success(result)
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }
}