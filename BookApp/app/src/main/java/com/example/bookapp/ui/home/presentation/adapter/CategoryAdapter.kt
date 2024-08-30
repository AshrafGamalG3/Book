package com.example.bookapp.ui.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.CategoryItemBinding
import com.example.bookapp.ui.home.data.model.CategoryModel

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.Holder>() {

    var categories: List<CategoryModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: OnItemClickListener? = null




    inner class Holder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryModel) {
            binding.categoryName.text = category.categoryName
            binding.deleteCategory.setOnClickListener {
                onItemClickListener?.onItemClick(category)
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

    interface OnItemClickListener {
        fun onItemClick(category: CategoryModel)
    }
}
