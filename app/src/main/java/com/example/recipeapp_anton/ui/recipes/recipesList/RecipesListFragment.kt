package com.example.recipeapp_anton.ui.recipes.recipesList

import android.graphics.drawable.Drawable
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
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.ui.recipes.recipesList.adapter.RecipesListAdapter
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.databinding.FragmentListRecipesBinding
import com.example.recipeapp_anton.model.Recipe
import com.example.recipeapp_anton.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private val viewModel: RecipesListViewModel by viewModels()

    private val recipesListAdapter: RecipesListAdapter = RecipesListAdapter()

    private var categoryId: Int? = null

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListRecipesBinding in null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
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
        categoryId = requireArguments().getInt(Constants.Bundle.ARG_CATEGORY_ID, 0)
        val localCategoryId = categoryId
        if (localCategoryId != null) {
            viewModel.loadRecipesList(localCategoryId)
        } else {
            Log.i("Exception", "Recipe ID $categoryId not found")
        }
    }

    private fun setupViews() {
        setupRecyclerView()
    }

    private fun setupListeners() {
        recipesListAdapter.setOnItemClickListener { recipeId ->
            openRecipeByRecipeId(recipeId)
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            setTitleCategory(state.categoryName)
            setImageCategory(state.categoryImage)
            setRecycleViewData(state.recipeList)
        }
    }

    private fun setupRecyclerView() {
        binding.rvRecipes.adapter = recipesListAdapter
    }

    private fun setRecycleViewData(recipeListDataset: List<Recipe>) {
        recipesListAdapter.dataSet = recipeListDataset
    }

    private fun setImageCategory(drawable: Drawable?) = binding.ivRecipes.setImageDrawable(drawable)

    private fun setTitleCategory(categoryName: String?) {
        binding.tvCategories.text = categoryName
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = bundleOf(Constants.Bundle.ARG_RECIPE_ID to recipeId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }
}