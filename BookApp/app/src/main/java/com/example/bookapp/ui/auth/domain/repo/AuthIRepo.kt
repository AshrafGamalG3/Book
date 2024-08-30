package com.example.bookapp.ui.auth.domain.repo

import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

interface AuthIRepo {

    suspend fun createAccountWithEmailAndPass(user: User, password: String): Resource<User>
    suspend fun loginWithEmailAndPass(email: String, password: String): Resource<FirebaseUser>
    suspend fun resetPassword(email: String): Resource<String>
}