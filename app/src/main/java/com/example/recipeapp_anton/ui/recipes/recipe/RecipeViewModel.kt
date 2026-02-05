package com.example.recipeapp_anton.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipeapp_anton.model.Recipe

data class RecipeState (
    val recipe: Recipe? = null,
    val portions: Int = 1,
    val isFavorite: Boolean = false
) {
    val title = recipe?.title ?: ""
    val methods = recipe?.method ?: emptyList()
    val ingredients = recipe?.ingredients ?: emptyList()
    val imageUrl = null
}

class RecipeViewModel : ViewModel() {}