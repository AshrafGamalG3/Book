package com.example.bookapp.utils

import android.content.Context
import android.net.Uri

fun validateTitle(title: String): ValidationResult {
    return when {
        title.isBlank() -> ValidationResult.Failed("Title cannot be empty")
        title.length !in 3..100 -> ValidationResult.Failed("Title must be between 3 and 100 characters")
        else -> ValidationResult.Success
    }
}

fun validateDescription(description: String): ValidationResult {
    return when {
        description.isBlank() -> ValidationResult.Failed("Description cannot be empty")
        description.length !in 10..1000 -> ValidationResult.Failed("Description must be between 10 and 1000 characters")
        else -> ValidationResult.Success
    }
}

fun validateCategory(category: String): ValidationResult {
    return when {
        category.isBlank() -> ValidationResult.Failed("Category cannot be empty")
        else -> ValidationResult.Success
        }
}

fun validatePdfUri(pdfUri: String?): ValidationResult {
    return when {
        pdfUri == null -> ValidationResult.Failed("No PDF file selected")
        else -> ValidationResult.Success
    }
}

