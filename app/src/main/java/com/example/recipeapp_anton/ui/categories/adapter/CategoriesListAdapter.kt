package com.example.recipeapp_anton.ui.categories.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp_anton.databinding.ItemCategoryBinding
import com.example.recipeapp_anton.model.Category

class CategoriesListAdapter() : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    var dataSet: List<Category> = emptyList()

    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
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

        val drawable = try {
            viewHolder.itemView.context.assets
                .open(category.imageUrl)
                .use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
        } catch (e: Exception) {
            Log.i("catch exception", "Image not found: ${category.imageUrl}")
            null
        }
        viewHolder.imageView.setImageDrawable(drawable)

        viewHolder.cardView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
            Log.i("Регистрация клика", "Произошло нажатие на категорию: ${category.id}")
        }
    }

    override fun getItemCount() = dataSet.size
}