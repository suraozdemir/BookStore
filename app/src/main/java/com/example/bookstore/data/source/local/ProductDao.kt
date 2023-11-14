package com.example.bookstore.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM fav_products")
    suspend fun getProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(product: ProductEntity)

    @Query("SELECT id FROM fav_products")
    suspend fun getProductId(): List<Int>

    @Delete
    suspend fun deleteFromFavorites(product: ProductEntity)

    @Query("DELETE FROM fav_products")
    suspend fun clearFavorites()
}