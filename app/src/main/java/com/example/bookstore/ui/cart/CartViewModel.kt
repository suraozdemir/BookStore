package com.example.bookstore.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.common.Resource
import com.example.bookstore.data.model.request.ClearCartRequest
import com.example.bookstore.data.model.response.BaseResponse
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.data.repo.ProductRepository
import com.example.bookstore.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState>
        get() = _cartState

    private val _totalPriceAmount = MutableLiveData(0.0)
    val totalPriceAmount: LiveData<Double> = _totalPriceAmount

    fun getCartProducts(userId: String) {
        viewModelScope.launch {
            val result = productRepository.getCartProducts(userId)

            when (result) {
                is Resource.Success -> {
                    _cartState.value = CartState.Data(result.data)
                    _totalPriceAmount.value = result.data.sumOf { product ->
                        if (product.saleState) {
                            product.price
                        } else {
                            product.price
                        }
                    }
                }

                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                    resetTotalAmount()
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    fun deleteFromCart(id: Int) {
        viewModelScope.launch {
            productRepository.deleteFromCart(userRepository.getUserUid(), id)
            getCartProducts(userRepository.getUserUid())
            resetTotalAmount()
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            val clearCartRequest = ClearCartRequest(userId)
            when (val result = productRepository.clearCart(clearCartRequest)) {
                is Resource.Success -> {
                    _cartState.value = CartState.ClearCart(result.data)
                    getCartProducts(userRepository.getUserUid())
                }

                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }

                is Resource.Fail -> TODO()
            }
        }
    }

    private fun resetTotalAmount() {
        _totalPriceAmount.value = 0.0
    }

}

sealed interface CartState {
    data class Data(val products: List<ProductUI>) : CartState
    data class Error(val throwable: Throwable) : CartState
    data class DeleteFromCart(val baseResponse: BaseResponse) : CartState
    data class ClearCart(val baseResponse: BaseResponse) : CartState
}