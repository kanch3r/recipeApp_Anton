package com.example.recipeapp_anton.ui.recipes.favorites

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Recipe

data class FavoritesUiState(
    val recipeList: List<Recipe> = emptyList(),
    val isFavoriteListEmpty: Boolean = true,
    val isFavoriteListVisible: Boolean = false
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val repository = RecipesRepository()

    private val mainHandler = Handler(Looper.getMainLooper())

    private val _state = MutableLiveData(FavoritesUiState())

    val state: LiveData<FavoritesUiState> = _state

    fun loadFavoritesRecipesList() {

        repository.getRecipes(appContext) { recipes ->
            mainHandler.post {
                when {
                    recipes == null -> Toast.makeText(
                        appContext,
                        "Ошибка",
                        Toast.LENGTH_SHORT
                    ).show()

                    recipes.isEmpty() -> _state.value = _state.value?.copy(
                        isFavoriteListEmpty = true,
                        isFavoriteListVisible = false
                    )

                    else -> _state.value = _state.value?.copy(
                        isFavoriteListEmpty = false,
                        isFavoriteListVisible = true,
                        recipeList = recipes
                    )
                }
            }
        }
    }
}