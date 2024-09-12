package com.example.bookapp.ui.auth.domain.usecase

import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.auth.domain.repo.AuthIRepo
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val iRepo: AuthIRepo)  {

    suspend fun createAccountWithEmailAndPass(user: User, password: String) : Resource<User> {
        return iRepo.createAccountWithEmailAndPass(user, password)
    }
    suspend fun loginWithEmailAndPass(email: String, password: String) : Resource<FirebaseUser> {
        return iRepo.loginWithEmailAndPass(email, password)
    }
    suspend fun resetPassword(email: String) : Resource<String> {
        return iRepo.resetPassword(email)
    }
    suspend fun getUserById(uid: String) : Resource<User> {
        return iRepo.getUserById(uid)

    }

}