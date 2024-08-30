package com.example.bookapp.utils

sealed class RegisterValidation {
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()

}
data class RegisterFieldsState(
    val emailValidation: RegisterValidation,
    val passwordValidation: RegisterValidation,
    val nameValidation: RegisterValidation,
    val confirmPasswordValidation: RegisterValidation
)