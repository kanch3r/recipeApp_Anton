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

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, payloads: List<Any>) {
        val ingredients = dataSet[position]

        with(viewHolder) {
            if (payloads.contains(Constants.PAYLOAD_UPDATE_QUANTITY)) {
                ingredientQuantityAndMeasures.text = calculateQuantity(ingredients)
                Log.i("!!!", "payload = TRUE")
            } else {
                ingredientName.text = ingredients.description
                ingredientQuantityAndMeasures.text = calculateQuantity(ingredients)
                Log.i("!!!", "payload = false")
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        onBindViewHolder(viewHolder, position, emptyList())
        Log.i("!!!", " выполнился onBindViewHolder БЕЗ payloads")
    }

    override fun getItemCount() = dataSet.size

    fun updateIngredients(progress: Int) {
        multiplier = progress
        notifyItemRangeChanged(0, itemCount, Constants.PAYLOAD_UPDATE_QUANTITY)
    }

    private fun calculateQuantity(ingredient: Ingredient): String {
        return if (multiplier == Constants.DEFAULT_MULTIPLIER) {
            "${ingredient.quantity} ${ingredient.unitOfMeasure}"
        } else {
            val newIngredientQuantity =
                if (ingredient.quantity.toBigDecimalOrNull() != null) {
                    ingredient.quantity.toBigDecimal().multiply(multiplier.toBigDecimal())
                        .stripTrailingZeros()
                        .toPlainString()
                } else {
                    Log.i(
                        "catch exception",
                        "Ошибка: некорректное значение ингредиента '${ingredient.quantity}'"
                    )
                    null
                }
            "$newIngredientQuantity ${ingredient.unitOfMeasure}"
        }
    }
}