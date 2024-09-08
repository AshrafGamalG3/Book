package com.example.bookapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.bookapp.ui.admin.data.model.CategoryModel


class AlertDialogCategory(
    private val context: Context,
    private val categoryNames: List<CategoryModel>,
    private val onCategorySelected: (String) -> Unit
) {

    fun show() {

        val items = categoryNames.map { it.categoryName }.toTypedArray()


        AlertDialog.Builder(context)
            .setTitle("Pick a category")
            .setItems(items) { _, which ->

                val selectedCategory = categoryNames[which]
                onCategorySelected(selectedCategory.categoryName)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
