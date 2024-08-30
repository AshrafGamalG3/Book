package com.example.bookapp.ui.home.domain.repo

import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.utils.Resource

interface HomeIRepo {

    suspend fun addCategory(category: CategoryModel): Resource<CategoryModel>

    suspend fun getCategories(): Resource<List<CategoryModel>>

    suspend fun deleteCategory(category: CategoryModel): Resource<CategoryModel>




}