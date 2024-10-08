package com.example.bookapp.ui.home.data.mapper

import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.data.model.CategoryModel
import javax.inject.Inject

class HomeMapper @Inject constructor()  {

    fun sortCategoriesByName(categories: List<CategoryModel>): List<CategoryModel> {
        return categories.sortedBy { it.categoryName }
    }

    fun sortBooksByName(books: List<BookModel>): List<BookModel> {
        return books.sortedBy { it.title }
    }
}