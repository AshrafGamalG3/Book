package com.example.bookapp.ui.home.command

import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.presentation.AppViewModel
import javax.inject.Inject

class UpdateBookCommand @Inject constructor(
    private val viewModel: AppViewModel,
    private val bookModel: BookModel
    ) :Command {
    override fun execute() {
        viewModel.updateBook(bookModel)
    }


}