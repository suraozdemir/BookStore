package com.example.bookstore.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.common.Resource
import com.example.bookstore.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _state = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState>
        get() = _state

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            if (checkInfo(email, password)) {
                _state.value = SignUpState.Loading

                _state.value = when (val result = userRepository.signUp(email, password)) {
                    is Resource.Success -> SignUpState.GoToHome
                    is Resource.Fail -> SignUpState.ShowPopUp(result.message)
                    is Resource.Error -> SignUpState.ShowPopUp(result.throwable.toString())
                }

            }
        }
    }

    private fun checkInfo(email: String, password: String): Boolean {
        return when {
            Patterns.EMAIL_ADDRESS.matcher(email).matches().not() -> {
                _state.value = SignUpState.ShowPopUp("Not a valid email address.")
                false
            }

            password.isEmpty() -> {
                _state.value = SignUpState.ShowPopUp("Password cannot be left blank.")
                false
            }

            email.isEmpty() -> {
                _state.value = SignUpState.ShowPopUp("Email cannot be left blank")
                false
            }

            password.length < 6 -> {
                _state.value = SignUpState.ShowPopUp("Password must be at least 6 characters")
                false
            }

            else -> true
        }
    }
}

sealed interface SignUpState {
    object Loading : SignUpState
    object GoToHome : SignUpState
    data class ShowPopUp(val errorMessage: String) : SignUpState
}
