package com.example.bookapp.ui.home.strategy

import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.utils.Resource
import javax.inject.Inject

class FavoriteBooksManager @Inject constructor(
    private val strategy: FavoriteStrategy
) {
    suspend fun addFavorite(bookModel: BookModel): Resource<BookModel> {
        return strategy.addFavorite(bookModel)
    }
    suspend fun deleteFavorite(bookModel: BookModel): Resource<BookModel> {
        return strategy.deleteFavorite(bookModel)
    }

}