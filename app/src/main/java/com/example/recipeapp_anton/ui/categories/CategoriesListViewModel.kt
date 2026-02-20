package com.example.recipeapp_anton.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp_anton.data.STUB
import com.example.recipeapp_anton.model.Category

data class CategoriesListUiState(
    val categoryList: List<Category> = emptyList(),
)

class CategoriesListViewModel() : ViewModel() {

    private val _state = MutableLiveData(CategoriesListUiState())

    val state: LiveData<CategoriesListUiState> = _state

    fun loadCategoryList() {
        val categoryList = STUB.getCategories()
        _state.value = _state.value?.copy(
            categoryList = categoryList
        )
    }
}