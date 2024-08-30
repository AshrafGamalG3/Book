package com.example.bookapp.ui.auth.data.model

data class User(

    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    var imagePath:String

)
{
    constructor():this("","","","","")
}
