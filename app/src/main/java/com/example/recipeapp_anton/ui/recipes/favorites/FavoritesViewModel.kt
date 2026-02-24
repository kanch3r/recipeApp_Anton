package com.example.recipeapp_anton.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp_anton.data.FavoritesSharedPreferences
import com.example.recipeapp_anton.data.STUB
import com.example.recipeapp_anton.model.Recipe

data class FavoritesUiState(
    val recipeList: List<Recipe> = emptyList(),
    val isFavoriteListEmpty: Boolean = true,
    val isFavoriteListVisible: Boolean = false
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData(FavoritesUiState())
    val state: LiveData<FavoritesUiState> = _state

    fun loadFavoritesRecipesList() {

        val favoritesList = FavoritesSharedPreferences.getFavorites(getApplication())
            .mapNotNull { it.toIntOrNull() }.toSet()

        if (favoritesList.isEmpty()) {
            _state.value = _state.value?.copy(
                isFavoriteListEmpty = true,
                isFavoriteListVisible = false
            )
        } else {
            val favoriteListRecipes = STUB.getRecipesByIds(favoritesList)
            _state.value = _state.value?.copy(
                isFavoriteListEmpty = false,
                isFavoriteListVisible = true,
                recipeList = favoriteListRecipes
            )
        }
    }
}