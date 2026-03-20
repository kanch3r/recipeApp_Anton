package com.example.recipeapp_anton.ui.categories

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Category

data class CategoriesListUiState(
    val categoryList: List<Category> = emptyList(),
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val repository = RecipesRepository()

    private val mainHandler = Handler(Looper.getMainLooper())

    private val _state = MutableLiveData(CategoriesListUiState())

    val state: LiveData<CategoriesListUiState> = _state


    fun loadCategoryList() {
        repository.getCategories { categories ->
            mainHandler.post {
                if (categories != null)
                    _state.value = _state.value?.copy(
                        categoryList = categories
                    ) else {
                    Toast.makeText(
                        appContext,
                        "Ошибка получения данных",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}