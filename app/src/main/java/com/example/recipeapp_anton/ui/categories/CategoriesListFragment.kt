package com.example.recipeapp_anton.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipeapp_anton.ui.categories.adapter.CategoriesListAdapter
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.data.STUB
import com.example.recipeapp_anton.databinding.FragmentListCategoriesBinding
import com.example.recipeapp_anton.ui.recipes.recipesList.RecipesListFragment

class CategoryItemClickListener(val clickOnItem: (Int) -> Unit) :
    CategoriesListAdapter.OnItemClickListener {
    override fun onItemClick(categoryId: Int) {
        clickOnItem(categoryId)
    }
}

class CategoriesListFragment : Fragment() {

    val viewModel: CategoriesListViewModel by viewModels()

    val categoriesListAdapter: CategoriesListAdapter = CategoriesListAdapter()

    private var _binding: FragmentListCategoriesBinding? = null

    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding in null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryView()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCategoryView() {
        viewModel.loadCategoryList()

        binding.rvCategories.adapter = categoriesListAdapter

        //listener

        categoriesListAdapter.setOnItemClickListener(CategoryItemClickListener { categoryId ->
            openRecipesByCategoryId(categoryId)
        })

        viewModel.state.observe(viewLifecycleOwner) { state ->
            categoriesListAdapter.dataSet = state.categoryList
        }

    }

    private fun openRecipesByCategoryId(categoryId: Int) {

        val bundle = bundleOf(Constants.Bundle.ARG_CATEGORY_ID to categoryId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
        }
    }
}