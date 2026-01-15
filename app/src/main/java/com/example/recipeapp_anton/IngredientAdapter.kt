package com.example.recipeapp_anton

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp_anton.databinding.IngredientRecipeBinding
import models.Ingredient

class IngredientAdapter(val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    class ViewHolder(binding: IngredientRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val ingredientName: TextView = binding.tvIngredientName
        val ingredientQuantity: TextView = binding.tvIngredientQuantity
        val ingredientMeasure: TextView = binding.tvIngredientMeasure
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = IngredientRecipeBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredients = dataSet[position]
        viewHolder.ingredientName.text = ingredients.description
        viewHolder.ingredientQuantity.text = ingredients.quantity
        viewHolder.ingredientMeasure.text = ingredients.unitOfMeasure
    }

    override fun getItemCount() = dataSet.size
}