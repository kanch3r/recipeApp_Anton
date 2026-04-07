package com.example.recipeapp_anton.ui.categories.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.databinding.ItemCategoryBinding
import com.example.recipeapp_anton.model.Category

class CategoriesListAdapter() : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    var dataSet: List<Category> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivCategoryCard
        val titleTextView: TextView = binding.tvTitleCategoryCard
        val descriptionTextView: TextView = binding.tvDescriptionCategoryCard
        val cardView: CardView = binding.cvCategoryElement
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category: Category = dataSet[position]

        viewHolder.titleTextView.text = category.title
        viewHolder.descriptionTextView.text = category.description

        val fullImageUrl = Constants.ApiConstants.BASE_URL_IMAGES + category.imageUrl

        Glide.with(viewHolder.imageView)
            .load(fullImageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.imageView)

        viewHolder.cardView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
            Log.i("Регистрация клика", "Произошло нажатие на категорию: ${category.id}")
        }
    }

    override fun getItemCount() = dataSet.size
}