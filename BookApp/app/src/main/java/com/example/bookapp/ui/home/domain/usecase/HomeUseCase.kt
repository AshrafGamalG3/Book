package com.example.bookapp.ui.home.domain.usecase

import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.domain.repo.HomeIRepo
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val repo: HomeIRepo) {

    suspend fun addCategory(category: CategoryModel) = repo.addCategory(category)
    suspend fun getCategories() = repo.getCategories()

    suspend fun deleteCategory(category: CategoryModel) = repo.deleteCategory(category)

    suspend fun addBook(bookModel: BookModel) = repo.addBook(bookModel)

    suspend fun getBooksByCategory(categoryName: String) = repo.getBooksByCategory(categoryName)

    suspend fun deleteBook(bookModel: BookModel) = repo.deleteBook(bookModel)

    suspend fun updateBook(bookModel: BookModel) = repo.updateBook(bookModel)

    suspend fun changeFavorite(bookModel: BookModel) = repo.addFavorite(bookModel)

    suspend fun deleteFavorite(bookModel: BookModel) = repo.deleteFavorite(bookModel)

    suspend fun isFavorite(bookModel: BookModel) = repo.isFavorite(bookModel)

    suspend fun getFavoriteBooks() = repo.getFavoriteBooks()

    suspend fun updateProfile(user: User) = repo.updateProfile(user)


}