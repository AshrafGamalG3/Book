package com.example.bookapp.ui.auth.domain.repo

import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.auth.data.repo.AuthIRepo
import com.example.bookapp.utils.Constants.USER_COLLECTION
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.rajat.pdfviewer.PdfDownloader
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class AuthRepo @Inject constructor(private val firebaseAuth: FirebaseAuth,private val db: FirebaseFirestore) :
    AuthIRepo {
    override suspend fun createAccountWithEmailAndPass(user: User, password: String): Resource<User> {
        return try {
            val register=firebaseAuth.createUserWithEmailAndPassword(user.email,password).await()

            register.user.let {
              db.collection(USER_COLLECTION).document(it!!.uid).set(user).await()
                Resource.Success(user)
            }

        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun loginWithEmailAndPass(email: String, password: String): Resource<FirebaseUser> {
            return try {
              val login=firebaseAuth.signInWithEmailAndPassword(email,password).await()
               Resource.Success(login.user!!)
            }catch (e:Exception){
                Resource.Error(e.message.toString())
            }
    }

    override suspend fun resetPassword(email: String): Resource<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(email)
        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }

    }

    override suspend fun getUserById(uid: String): Resource<User> {
        return try {
            val user=db.collection(USER_COLLECTION).document(uid).get().await().toObject(User::class.java)
            Resource.Success(user!!)
        }catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }


}