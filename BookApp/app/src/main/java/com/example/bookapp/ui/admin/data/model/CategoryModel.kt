package com.example.bookapp.ui.admin.data.model

data class CategoryModel(

    val categoryName: String,
    val userId: String,
    val timestamp: Long,
){
    constructor() : this("", "", 0)
}
