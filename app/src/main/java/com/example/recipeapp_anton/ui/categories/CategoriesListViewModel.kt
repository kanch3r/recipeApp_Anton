package com.example.recipeapp_anton.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.RecipesRepository
import com.example.recipeapp_anton.model.Category
import kotlinx.coroutines.launch

data class CategoriesListUiState(
    val categoryList: List<Category> = emptyList(),
    val errorMessage: String? = null,
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val repository = RecipesRepository(application)

    private val _state = MutableLiveData(CategoriesListUiState())

    val state: LiveData<CategoriesListUiState> = _state

    fun loadCategoryList() {
        viewModelScope.launch {
            val cachedCategories = repository.getCategoriesFromCache()
            if (cachedCategories.isNotEmpty()) {
                _state.value = _state.value?.copy(
                    categoryList = cachedCategories,
                    errorMessage = null
                )
            }

            val categoriesFromNet = repository.getCategories()
            if (categoriesFromNet != null) {
                _state.value = _state.value?.copy(
                    categoryList = categoriesFromNet,
                    errorMessage = null
                )
                repository.saveToDatabase(categoriesFromNet)
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