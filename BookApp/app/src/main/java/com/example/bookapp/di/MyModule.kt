package com.example.bookapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE

import com.example.bookapp.ui.auth.domain.repo.AuthRepo
import com.example.bookapp.ui.auth.data.repo.AuthIRepo
import com.example.bookapp.ui.admin.domain.repo.HomeRepo
import com.example.bookapp.ui.admin.data.repo.HomeIRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MyModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth()= FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore()= Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseStorage()= FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDataBase()= FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepo(firebaseAuth: FirebaseAuth,db: FirebaseFirestore): AuthIRepo = AuthRepo(firebaseAuth,db)

    @Provides
    @Singleton
    fun provideHomeRepo(db: FirebaseFirestore,storage: FirebaseStorage): HomeIRepo = HomeRepo(db,storage)



    @Provides
    @Singleton
    fun provideSharedPreferences(
        application: Application

    )= application.getSharedPreferences("app_preferences",MODE_PRIVATE)



}