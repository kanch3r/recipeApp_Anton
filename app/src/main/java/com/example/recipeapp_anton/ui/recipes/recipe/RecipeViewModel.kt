package com.example.recipeapp_anton.ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.data.FavoritesSharedPreferences
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Recipe
import kotlinx.coroutines.launch

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portions: Int = Constants.DEFAULT_MULTIPLIER,
    val isFavorite: Boolean = false,
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
            val recipe = repository.getRecipeByRecipeId(recipeId)
            val recipeImageUrl = Constants.ApiConstants.BASE_URL_IMAGES + recipe?.imageUrl

            if (recipe != null) {
                _state.value = _state.value?.copy(
                    recipe = recipe,
                    isFavorite = checkFavoriteStatus(recipeId),
                    recipeImageUrl = recipeImageUrl,
                    errorMessage = null,
                )
            } else {
                _state.value = _state.value?.copy(
                    errorMessage = appContext.getString(R.string.error_loading_data)
                )
            }
        }
    }

    fun updatePortions(newPortions: Int) {
        _state.value = _state.value?.copy(portions = newPortions)
    }

    private fun checkFavoriteStatus(recipeId: Int): Boolean {
        val favoriteList = FavoritesSharedPreferences.getFavorites(appContext)
        return favoriteList.contains(recipeId.toString())
    }

    fun onFavoritesClicked(recipeId: Int?) {
        val favoriteList = FavoritesSharedPreferences.getFavorites(appContext)
        if (favoriteList.contains(recipeId.toString())) {
            favoriteList.remove(recipeId.toString())
            _state.value = _state.value?.copy(isFavorite = false)
            Log.i("SP", "Рецепт $recipeId удалён из избранного")
        } else {
            favoriteList.add(recipeId.toString())
            Log.i("SP", "Рецепт $recipeId добавлен в избранное")
            _state.value = _state.value?.copy(isFavorite = true)
        }
        FavoritesSharedPreferences.saveFavorite(appContext, favoriteList)
    }

    fun clearErrorMessage() {
        _state.value = _state.value?.copy(errorMessage = null)
    }
}