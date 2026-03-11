package com.example.recipeapp_anton.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp_anton.data.STUB
import com.example.recipeapp_anton.model.Category
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

    fun loadRecipesList(category: Category) {

        val recipesList = STUB.getRecipesByCategoryId(category.id)
        val categoryName = category.title
        val categoryImage = category.imageUrl
        val drawable = try {
            appContext.assets
                .open(categoryImage)
                .use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
        } catch (e: Exception) {
            Log.e("catch exception", "Image not found: $categoryImage")
            null
        }

        _state.value = _state.value?.copy(
            recipeList = recipesList,
            categoryImage = drawable,
            categoryName = categoryName,
        )
    }
}