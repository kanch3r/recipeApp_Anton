package com.example.recipeapp_anton.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp_anton.data.STUB
import com.example.recipeapp_anton.model.Recipe

data class RecipesListUiState(
    val recipeList: List<Recipe> = emptyList(),
    val categoryImage: Drawable? = null,
    val categoryName: String? = "",
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val _state = MutableLiveData(RecipesListUiState())

    val state: LiveData<RecipesListUiState> = _state

    fun loadRecipesList(categoryId: Int) {

        val recipesList = STUB.getRecipesByCategoryId(categoryId)
        val category = STUB.getCategories().find { it.id == categoryId }
        val categoryName = category?.title
        val categoryImage = category?.imageUrl
        val drawable = if (categoryImage != null) {
            try {
                appContext.assets
                    .open(categoryImage)
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            } catch (e: Exception) {
                Log.e("catch exception", "Image not found: $categoryImage")
                null
            }
        } else {
            null
        }

        _state.value = _state.value?.copy(
            recipeList = recipesList,
            categoryImage = drawable,
            categoryName = categoryName,
        )
    }
}