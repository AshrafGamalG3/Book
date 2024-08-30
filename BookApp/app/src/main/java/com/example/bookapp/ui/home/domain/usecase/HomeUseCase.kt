package com.example.bookapp.ui.home.domain.usecase

import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.domain.repo.HomeIRepo
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val repo: HomeIRepo) {

    suspend fun addCategory(category: CategoryModel) = repo.addCategory(category)
    suspend fun getCategories() = repo.getCategories()

    suspend fun deleteCategory(category: CategoryModel) = repo.deleteCategory(category)


}