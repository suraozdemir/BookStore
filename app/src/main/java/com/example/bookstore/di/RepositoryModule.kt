package com.example.bookstore.di

import com.example.bookstore.data.repo.ProductRepository
import com.example.bookstore.data.repo.UserRepository
import com.example.bookstore.data.source.local.ProductDao
import com.example.bookstore.data.source.remote.ProductService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        productService: ProductService,
        productDao: ProductDao
    ): ProductRepository =
        ProductRepository(productService, productDao)

    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuth: FirebaseAuth): UserRepository =
        UserRepository(firebaseAuth)
}