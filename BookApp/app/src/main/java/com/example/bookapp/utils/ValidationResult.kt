package com.example.bookapp.utils

sealed  class ValidationResult {
    object Success : ValidationResult()
    data class Failed(val message: String) : ValidationResult()
}

data class BookValidationResult(
    val titleResult: ValidationResult,
    val descriptionResult: ValidationResult,
    val categoryResult: ValidationResult,
    val pdfUriResult: ValidationResult
)

