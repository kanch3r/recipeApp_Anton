package com.example.recipeapp_anton.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp_anton.ui.categories.adapter.CategoriesListAdapter
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.databinding.FragmentListCategoriesBinding
import com.example.recipeapp_anton.model.Category

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
        parseArguments()
        setupViews()
        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun parseArguments() {
        viewModel.loadCategoryList()
    }

    private fun setupViews() {
        binding.rvCategories.adapter = categoriesListAdapter
    }

    private fun setupListeners() {
        categoriesListAdapter.setOnItemClickListener { categoryId ->
            openRecipesByCategoryId(categoryId)
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            setRecycleViewData(state.categoryList)
        }
    }

    private fun setRecycleViewData(categoryListDataset: List<Category>) {
        categoriesListAdapter.dataSet = categoryListDataset
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val bundle = bundleOf(Constants.Bundle.ARG_CATEGORY_ID to categoryId)
        findNavController().navigate(R.id.recipesListFragment, args = bundle)
    }
}