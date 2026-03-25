package com.example.recipeapp_anton.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Category
import com.example.recipeapp_anton.model.Recipe

data class RecipesListUiState(
    val recipeList: List<Recipe> = emptyList(),
    val categoryImage: Drawable? = null,
    val categoryName: String? = "",
    val errorMessage: String? = null,
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val repository = RecipesRepository()

    private val mainHandler = Handler(Looper.getMainLooper())

    private val _state = MutableLiveData(RecipesListUiState())

    val state: LiveData<RecipesListUiState> = _state

    fun loadRecipesList(category: Category) {

        repository.getRecipesByCategoryId(category.id) { recipes ->
            mainHandler.post {
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

                if (recipes != null) {
                    _state.value = _state.value?.copy(
                        recipeList = recipes,
                        categoryImage = drawable,
                        categoryName = categoryName,
                        errorMessage = null,
                    )
                } else {
                    _state.value = _state.value?.copy(
                        errorMessage = appContext.getString(R.string.error_loading_data)
                    )
                }
            }
        }
    }

    fun clearErrorMessage() {
        _state.value = _state.value?.copy(errorMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        repository.shutdown()
    }
}