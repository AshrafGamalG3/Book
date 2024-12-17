package com.example.bookapp.ui.home.strategy

import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.utils.Resource

interface FavoriteStrategy {
    suspend fun addFavorite(bookModel: BookModel): Resource<BookModel>
    suspend fun deleteFavorite(bookModel: BookModel): Resource<BookModel>
}