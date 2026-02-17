package com.example.recipeapp_anton.ui.recipes.recipesList.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp_anton.databinding.ItemRecipeBinding
import com.example.recipeapp_anton.model.Recipe

class RecipesListAdapter() : RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    var dataSet: List<Recipe> = emptyList()

    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class ViewHolder(binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivRecipeCard
        val titleTextView: TextView = binding.tvTitleRecipeCard

        val cardView: CardView = binding.cvRecipeElement
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]

        viewHolder.titleTextView.text = recipe.title

        val drawable = try {
            viewHolder.itemView.context.assets
                .open(recipe.imageUrl)
                .use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
        } catch (e: Exception) {
            Log.i("catch exception", "Image not found: ${recipe.imageUrl}")
            null
        }
        viewHolder.imageView.setImageDrawable(drawable)

        viewHolder.cardView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
            Log.i("Регистрация клика", "Произошло нажатие на категорию: ${recipe.id}")
        }
    }

    override fun getItemCount() = dataSet.size
}