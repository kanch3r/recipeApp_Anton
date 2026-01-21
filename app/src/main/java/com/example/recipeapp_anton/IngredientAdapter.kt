package com.example.recipeapp_anton

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp_anton.databinding.IngredientRecipeBinding
import models.Ingredient

class IngredientAdapter(val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    private var multiplier: Int = Constants.DEFAULT_MULTIPLIER

    class ViewHolder(binding: IngredientRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val ingredientName: TextView = binding.tvIngredientName
        val ingredientQuantityAndMeasures: TextView = binding.tvIngredientQuantityAndMeasures
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = IngredientRecipeBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredients = dataSet[position]
        viewHolder.ingredientName.text = ingredients.description

        if (multiplier == Constants.DEFAULT_MULTIPLIER) {
            viewHolder.ingredientQuantityAndMeasures.text =
                "${ingredients.quantity} ${ingredients.unitOfMeasure}"
        } else {
            val newIngredientQuantity =
                when {
                    (ingredients.quantity.toIntOrNull() != null) ->
                        (ingredients.quantity.toInt() * multiplier).toString()

                    (ingredients.quantity.toDoubleOrNull() != null) ->
                        roundDoubleToInt(ingredients.quantity.toDouble() * multiplier)

                    else -> {
                        Log.i(
                            "catch exception",
                            "Ошибка: некорректное значение ингредиента '${ingredients.quantity}'"
                        )
                        null
                    }
                }
            viewHolder.ingredientQuantityAndMeasures.text =
                "$newIngredientQuantity ${ingredients.unitOfMeasure}"
        }
    }

    override fun getItemCount() = dataSet.size


    fun updateIngredients(progress: Int) {
        multiplier = progress
        notifyDataSetChanged()
    }

    private fun roundDoubleToInt(input: Double): String {
        return if (input % 1 == 0.0) input.toInt().toString() else input.toString()
    }
}