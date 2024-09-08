package com.example.bookapp.ui.admin.presentation.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.App.Companion.formatDate
import com.example.bookapp.App.Companion.getPdfSize
import com.example.bookapp.App.Companion.loadPdfFromUrlSinglePage
import com.example.bookapp.R
import com.example.bookapp.databinding.PdfRowItemBinding
import com.example.bookapp.ui.admin.data.model.BookModel

class BookAdapter : RecyclerView.Adapter<BookAdapter.Holder>() {

    var books: List<BookModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClickListener: OnItemClickListener? = null
    var searchQuery: String = ""
    var onItemClickListener2: onItemClickBookListener? = null


    fun updateCategories(books: List<BookModel>, searchQuery: String) {
        this.books = books
        this.searchQuery = searchQuery
        notifyDataSetChanged()
    }
    inner class Holder(private val binding: PdfRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: BookModel) {
            binding.titleText.text = bookModel.title
            binding.descriptionText.text = bookModel.description
            binding.categoryText.text = bookModel.categoryName
            binding.dateText.text = formatDate(bookModel.timestamp)
            getPdfSize(bookModel.pdfUrl) { size ->
                binding.pdfSize.text = size
            }

            binding.pdfImageView.setImageDrawable(null)
            binding.progressBar.visibility = View.VISIBLE

            loadPdfFromUrlSinglePage(bookModel.pdfUrl, binding.pdfImageView, binding.progressBar)
            binding.moreButton.setOnClickListener {
                onItemClickListener?.onItemClick(bookModel)
            }

            val startIndex = bookModel.title.lowercase().indexOf(searchQuery.lowercase())

            if (startIndex != -1) {
                val endIndex = startIndex + searchQuery.length
                val spannable = SpannableString(bookModel.title)
                val color = ContextCompat.getColor(binding.root.context, R.color.gray02)
                spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.titleText.text = spannable
            } else {
                binding.titleText.text = bookModel.title
            }

            binding.root.setOnClickListener {
                onItemClickListener2?.onItemClick(bookModel)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = PdfRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(books[position])
    }

    interface OnItemClickListener {
        fun onItemClick(bookModel: BookModel)
    }
    interface onItemClickBookListener{
        fun onItemClick(bookModel: BookModel)
    }
}
