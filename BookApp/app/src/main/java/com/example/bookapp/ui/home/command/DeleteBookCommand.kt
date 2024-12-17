package com.example.bookapp.ui.home.command

import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.presentation.AppViewModel
import javax.inject.Inject

class DeleteBookCommand @Inject constructor(
    private val viewModel: AppViewModel,
    private val book: BookModel

):Command {
    override fun execute() {
        viewModel.deleteBook(book)
    }
}