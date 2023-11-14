package com.example.bookstore.data.repo

import com.example.bookstore.common.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : UserRepo {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun checkUserLogin(): Boolean = firebaseAuth.currentUser != null

    fun getUserUid(): String = firebaseAuth.currentUser?.uid.orEmpty()

    suspend fun signUp(email: String, password: String): Resource<Boolean> {
        return try {
            val signUpTask = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(signUpTask.user != null)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signIn(email: String, password: String): Resource<Boolean> {
        return try {
            val signInTask = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(signInTask.user != null)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}

interface UserRepo {
    val currentUser: FirebaseUser?
}