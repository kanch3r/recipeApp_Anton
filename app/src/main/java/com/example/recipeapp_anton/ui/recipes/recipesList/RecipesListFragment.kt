package com.example.recipeapp_anton.ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.recipeapp_anton.ui.recipes.recipesList.adapter.RecipesListAdapter
import com.example.recipeapp_anton.databinding.FragmentListRecipesBinding
import com.example.recipeapp_anton.model.Recipe

class RecipesListFragment : Fragment() {

    private val viewModel: RecipesListViewModel by viewModels()

    private val recipesListAdapter: RecipesListAdapter = RecipesListAdapter()

    private val args: RecipesListFragmentArgs by navArgs()

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
        val localCategory = args.category
        viewModel.loadRecipesList(localCategory)
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
        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}