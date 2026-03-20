package com.example.recipeapp_anton.ui.recipes.recipe

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.data.FavoritesSharedPreferences
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portions: Int = Constants.DEFAULT_MULTIPLIER,
    val isFavorite: Boolean = false,
    val recipeImage: Drawable? = null
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val repository = RecipesRepository()

    private val mainHandler = Handler(Looper.getMainLooper())

    private val _state = MutableLiveData(RecipeUiState())
    val state: LiveData<RecipeUiState> = _state

    init {
        Log.i("HAPPY", "Here is init block execution")
    }

    fun loadRecipe(recipeId: Int) {
        repository.getRecipeByRecipeId(recipeId) { recipe ->
            mainHandler.post {
                val localImage = recipe?.imageUrl
                val drawable = if (localImage != null) {
                    try {
                        appContext.assets
                            .open(recipe.imageUrl)
                            .use { inputStream ->
                                Drawable.createFromStream(inputStream, null)
                            }
                    } catch (e: Exception) {
                        Log.e("catch exception", "Image not found: $localImage")
                        null
                    }
                } else {
                    null
                }

                if (recipe != null) {
                    _state.value = _state.value?.copy(
                        recipe = recipe,
                        isFavorite = checkFavoriteStatus(recipeId),
                        recipeImage = drawable
                    )
                } else {
                    Toast.makeText(
                        appContext,
                        "Ошибка получения данных",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
}