package com.example.recipeapp_anton.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp_anton.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val portions: Int = 1,
    val isFavorite: Boolean = false
)

class RecipeViewModel : ViewModel() {

    private val _state = MutableLiveData(RecipeState())
    val state: LiveData<RecipeState> = _state

    init {
        Log.i("HAPPY", "Here is init block execution")
        _state.value = _state.value?.copy(isFavorite = true)
    }

}