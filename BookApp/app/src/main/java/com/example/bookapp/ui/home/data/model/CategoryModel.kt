package com.example.bookapp.ui.home.data.model

data class CategoryModel(

    val categoryName: String,
    val userId: String,
    val timestamp: Long,
){
    constructor() : this("", "", 0)
}
