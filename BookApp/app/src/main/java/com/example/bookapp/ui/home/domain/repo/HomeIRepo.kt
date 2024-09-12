package com.example.bookapp.ui.home.domain.repo

import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.utils.Resource

interface HomeIRepo {

    suspend fun addCategory(category: CategoryModel): Resource<CategoryModel>

    suspend fun getCategories(): Resource<List<CategoryModel>>

    suspend fun deleteCategory(category: CategoryModel): Resource<CategoryModel>

    suspend fun addBook(bookModel: BookModel): Resource<BookModel>

    suspend fun getBooksByCategory(category: String): Resource<List<BookModel>>

    suspend fun deleteBook(bookModel: BookModel): Resource<BookModel>

    suspend fun updateBook(bookModel: BookModel): Resource<BookModel>

    suspend fun addFavorite(bookModel: BookModel): Resource<BookModel>

    suspend fun deleteFavorite(bookModel: BookModel): Resource<BookModel>

    suspend fun isFavorite(bookModel: BookModel): Resource<Boolean>

    suspend fun getFavoriteBooks(): Resource<List<BookModel>>

    suspend fun updateProfile(user: User): Resource<User>





}