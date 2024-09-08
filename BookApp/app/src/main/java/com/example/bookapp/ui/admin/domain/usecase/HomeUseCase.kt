package com.example.bookapp.ui.admin.domain.usecase

import com.example.bookapp.ui.admin.data.model.BookModel
import com.example.bookapp.ui.admin.data.model.CategoryModel
import com.example.bookapp.ui.admin.data.repo.HomeIRepo
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


}