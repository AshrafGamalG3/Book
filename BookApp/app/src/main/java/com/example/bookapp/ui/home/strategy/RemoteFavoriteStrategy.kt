package com.example.bookapp.ui.home.strategy

import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.domain.usecase.HomeUseCase
import com.example.bookapp.utils.Resource
import javax.inject.Inject

class RemoteFavoriteStrategy @Inject constructor(

    private val useCase: HomeUseCase
) :FavoriteStrategy {
    override suspend fun addFavorite(bookModel: BookModel): Resource<BookModel> {
        return  useCase.changeFavorite(bookModel);
    }

    override suspend fun deleteFavorite(bookModel: BookModel): Resource<BookModel> {
      return useCase.deleteFavorite(bookModel)
    }
}