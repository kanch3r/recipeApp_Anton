package com.example.recipeapp_anton.ui.recipes.recipesList.adapter

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
import com.example.recipeapp_anton.databinding.ItemRecipeBinding
import com.example.recipeapp_anton.model.Recipe

class RecipesListAdapter() : RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    var dataSet: List<Recipe> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
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

        Log.i("Recipe Image URL", "Relative path: ${recipe.imageUrl}")

        val fullImageUrl = Constants.ApiConstants.BASE_URL_IMAGES + recipe.imageUrl

        try {
            Glide.with(viewHolder.imageView)
                .load(fullImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(viewHolder.imageView)
        } catch (e: Exception) {
            Log.i("catch exception", "Image not found: ${recipe.imageUrl}")
        }

        viewHolder.cardView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
            Log.i("Регистрация клика", "Произошло нажатие на категорию: ${recipe.id}")
        }
    }

    override fun getItemCount() = dataSet.size
}