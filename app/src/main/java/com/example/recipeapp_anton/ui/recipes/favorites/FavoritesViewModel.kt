package com.example.recipeapp_anton.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Recipe
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val recipeList: List<Recipe> = emptyList(),
    val isFavoriteListEmpty: Boolean = true,
    val isFavoriteListVisible: Boolean = false,
    val errorMessage: String? = null,
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipesRepository(application)

    private val _state = MutableLiveData(FavoritesUiState())

    val state: LiveData<FavoritesUiState> = _state

    fun loadFavoritesRecipesList() {
        viewModelScope.launch {
            val recipes = repository.getFavoriteRecipes()
            if (recipes.isEmpty()) {
                _state.value = _state.value?.copy(
                    isFavoriteListEmpty = true,
                    isFavoriteListVisible = false,
                    errorMessage = null
                )
            } else {
                _state.value = _state.value?.copy(
                    isFavoriteListEmpty = false,
                    isFavoriteListVisible = true,
                    recipeList = recipes,
                    errorMessage = null
                )
            }
        }
    }

    fun clearErrorMessage() {
        _state.value = _state.value?.copy(errorMessage = null)
    }
}