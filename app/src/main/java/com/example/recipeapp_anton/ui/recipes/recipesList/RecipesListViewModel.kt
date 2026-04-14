package com.example.recipeapp_anton.ui.recipes.recipesList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Category
import com.example.recipeapp_anton.model.Recipe
import kotlinx.coroutines.launch

data class RecipesListUiState(
    val recipeList: List<Recipe> = emptyList(),
    val categoryImageUrl: String? = null,
    val categoryName: String? = "",
    val errorMessage: String? = null,
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val repository = RecipesRepository(application)

    private val _state = MutableLiveData(RecipesListUiState())

    val state: LiveData<RecipesListUiState> = _state

    fun loadRecipesList(category: Category) {
        viewModelScope.launch {
            val categoryName = category.title
            val categoryImageUrl = Constants.ApiConstants.BASE_URL_IMAGES + category.imageUrl

            val cachedRecipes = repository.getRecipesFromCache(category.id)
            if (cachedRecipes.isNotEmpty()) {
                _state.value = _state.value?.copy(
                    recipeList = cachedRecipes,
                    categoryImageUrl = categoryImageUrl,
                    categoryName = categoryName,
                    errorMessage = null,
                )
                Log.i("!!!", "Произошла загрузка рецептов ${category.title} из cache")
            }

            val recipesFromNet = repository.getRecipesByCategoryId(category.id)

            if (recipesFromNet != null) {
                _state.value = _state.value?.copy(
                    recipeList = recipesFromNet,
                    categoryImageUrl = categoryImageUrl,
                    categoryName = categoryName,
                    errorMessage = null,
                )
                repository.saveRecipesToDatabase(category.id, recipesFromNet)
                Log.i("!!!", "Произошла загрузка рецептов ${category.title} из сети")
            } else {
                _state.value = _state.value?.copy(
                    errorMessage = appContext.getString(R.string.error_loading_data)
                )
            }
        }
    }

    fun clearErrorMessage() {
        _state.value = _state.value?.copy(errorMessage = null)
    }
}