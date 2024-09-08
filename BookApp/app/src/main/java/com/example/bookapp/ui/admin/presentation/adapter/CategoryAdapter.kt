package com.example.bookapp.ui.admin.presentation.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.CategoryItemBinding
import com.example.bookapp.ui.admin.data.model.CategoryModel


class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.Holder>() {

    var categories: List<CategoryModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: OnDeleteClickListener? = null
    var onItemClickListener2: OnItemClickListener? = null

    var searchQuery: String = ""

    fun updateCategories(newCategories: List<CategoryModel>, searchQuery: String) {
        this.categories = newCategories
        this.searchQuery = searchQuery
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryModel) {
            binding.categoryName.text = category.categoryName
            binding.deleteCategory.setOnClickListener {
                onItemClickListener?.onDeleteClick(category)
            }
            binding.categoryItem.setOnClickListener {
                onItemClickListener2?.onItemClick(category.categoryName)

            }

            val startIndex = category.categoryName.lowercase().indexOf(searchQuery.lowercase())

            if (startIndex != -1) {
                val endIndex = startIndex + searchQuery.length
                val spannable = SpannableString(category.categoryName)
                val color = ContextCompat.getColor(binding.root.context, R.color.gray02)
                spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.categoryName.text = spannable
            } else {
                binding.categoryName.text = category.categoryName
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return  categories.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(categories[position])
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(category: CategoryModel)
    }
    interface OnItemClickListener {
        fun onItemClick(category: String)
    }
}
