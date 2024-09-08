package com.example.bookapp.ui.admin.data.repo

import com.example.bookapp.ui.admin.data.model.BookModel
import com.example.bookapp.ui.admin.data.model.CategoryModel
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





}