package com.example.bookstore.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.common.Resource
import com.example.bookstore.data.model.request.AddToCartRequest
import com.example.bookstore.data.model.response.BaseResponse
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.data.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun getProducts() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = productRepository.getProducts()
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun getSaleProducts() {
        viewModelScope.launch {
            val result = productRepository.getSaleProducts()
            when (result) {
                is Resource.Success -> {
                    _homeState.value = HomeState.SaleData(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun addToCart(addToCartRequest: AddToCartRequest) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            val result = productRepository.addToCart(addToCartRequest)
            if (result is Resource.Success) {
                _homeState.value = HomeState.Response(result.data)
            } else if (result is Resource.Error) {
                _homeState.value = HomeState.Error(result.throwable)
            }
        }
    }

    fun addToFavorites(product: ProductUI) {
        viewModelScope.launch {
            productRepository.addToFavorites(product)
        }
    }

    fun deleteFromFavorites(product: ProductUI) {
        viewModelScope.launch {
            productRepository.deleteFromFavorites(product)
        }
    }
}

sealed interface HomeState {
    object Loading : HomeState
    data class Data(val products: List<ProductUI>) : HomeState
    data class SaleData(val products: List<ProductUI>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
    data class Response(val crud: BaseResponse) : HomeState
}