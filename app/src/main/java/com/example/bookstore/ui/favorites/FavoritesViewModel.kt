package com.example.bookstore.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.common.Resource
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.data.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private var _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState>
        get() = _favoriteState


    fun getFavoriteProducts() {
        viewModelScope.launch {

            _favoriteState.value = FavoriteState.Loading

            val result = productRepository.getFavoriteProducts()
            if (result is Resource.Success) {
                _favoriteState.value = FavoriteState.Data(result.data)
            } else if (result is Resource.Error) {
                _favoriteState.value = FavoriteState.Error(result.throwable)
            }
        }
    }

    fun deleteFromFavorites(product: ProductUI) {
        viewModelScope.launch {
            productRepository.deleteFromFavorites(product)
            getFavoriteProducts()
        }
    }

    fun clearFavorites() {
        viewModelScope.launch {
            productRepository.clearFavorites()
            getFavoriteProducts()
        }
    }
}

sealed interface FavoriteState {
    object Loading : FavoriteState
    data class Data(val products: List<ProductUI>) : FavoriteState
    data class Error(val throwable: Throwable) : FavoriteState
}