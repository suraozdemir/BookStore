package com.example.bookstore.ui.signin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.common.Resource
import com.example.bookstore.data.repo.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    private val userRepo: UserRepository
) : ViewModel() {

    private var _State = MutableLiveData<SignInState>()
    val signInState: LiveData<SignInState>
        get() = _State

    val currentUser: FirebaseUser?
        get() = userRepo.currentUser

    fun signIn(email: String, password: String) = viewModelScope.launch {
        if (checkInfo(email, password)) {
            _State.value = SignInState.Loading

            _State.value = when (val result = userRepo.signIn(email, password)) {
                is Resource.Success -> SignInState.GoToHome
                is Resource.Fail -> SignInState.ShowPopUp(result.message)
                is Resource.Error -> SignInState.ShowPopUp(result.throwable.toString())
            }
        }
    }

    private fun checkInfo(email: String, password: String): Boolean {
        return when {
            Patterns.EMAIL_ADDRESS.matcher(email).matches().not() -> {
                _State.value = SignInState.ShowPopUp("Not a valid email address.")
                false
            }

            password.isEmpty() -> {
                _State.value = SignInState.ShowPopUp("Password cannot be left blank.")
                false
            }

            email.isEmpty() -> {
                _State.value = SignInState.ShowPopUp("Email cannot be left blank")
                false
            }

            password.length < 6 -> {
                _State.value = SignInState.ShowPopUp("Password must be at least 6 characters")
                false
            }

            else -> true
        }
    }
}

sealed interface SignInState {
    object Loading : SignInState
    object GoToHome : SignInState
    data class ShowPopUp(val error: String) : SignInState
}
