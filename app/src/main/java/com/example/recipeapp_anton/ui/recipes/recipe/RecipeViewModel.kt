package com.example.recipeapp_anton.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Recipe
import kotlinx.coroutines.launch

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portions: Int = Constants.DEFAULT_MULTIPLIER,
    val recipeImageUrl: String? = null,
    val errorMessage: String? = null,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val repository = RecipesRepository(application)

    private val _state = MutableLiveData(RecipeUiState())

    val state: LiveData<RecipeUiState> = _state

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val cacheRecipe = repository.getRecipeFromCache(recipeId)
            val cacheRecipeImageUrl = Constants.ApiConstants.BASE_URL_IMAGES + cacheRecipe?.imageUrl
            if (cacheRecipe != null) {
                _state.value = _state.value?.copy(
                    recipe = cacheRecipe,
                    recipeImageUrl = cacheRecipeImageUrl,
                    errorMessage = null
                )
            } else {
                _state.value = _state.value?.copy(
                    errorMessage = appContext.getString(R.string.error_loading_data)
                )
            }

            val recipeFromNet = repository.getRecipeByRecipeId(recipeId)
            val recipeFromNetImageUrl =
                Constants.ApiConstants.BASE_URL_IMAGES + recipeFromNet?.imageUrl

            if (recipeFromNet != null) {
                val favoriteStatusFromCache = cacheRecipe?.isFavorite ?: false
                val updatedRecipe = recipeFromNet.copy(isFavorite = favoriteStatusFromCache)

                _state.value = _state.value?.copy(
                    recipe = updatedRecipe,
                    recipeImageUrl = recipeFromNetImageUrl,
                    errorMessage = null,
                )
                repository.saveRecipesToDatabase(recipes = listOf(updatedRecipe))
            }

            if (cacheRecipe == null && recipeFromNet == null) {
                _state.value = _state.value?.copy(
                    errorMessage = appContext.getString(R.string.error_loading_data)
                )
            }
        }
    }

    fun updatePortions(newPortions: Int) {
        _state.value = _state.value?.copy(portions = newPortions)
    }

    fun onFavoritesClicked(recipeId: Int) {
        val currentState = _state.value ?: return
        val currentRecipe = currentState.recipe ?: return
        val newFavoriteStatus = !currentRecipe.isFavorite

        _state.value = currentState.copy(
            recipe = currentRecipe.copy(isFavorite = newFavoriteStatus)
        )

        viewModelScope.launch {
            repository.updateFavoriteStatus(recipeId, newFavoriteStatus)
        }
    }

    fun clearErrorMessage() {
        _state.value = _state.value?.copy(errorMessage = null)
    }
}