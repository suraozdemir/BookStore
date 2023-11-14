package com.example.bookstore.di

import android.content.Context
import androidx.room.Room
import com.example.bookstore.data.source.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ProductDatabase::class.java, "product_room_db").build()

    @Provides
    @Singleton
    fun provideDao(roomDB: ProductDatabase) = roomDB.productsDao()
}