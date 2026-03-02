package com.example.recipeapp_anton.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.ui.recipes.recipesList.adapter.RecipesListAdapter
import com.example.recipeapp_anton.databinding.FragmentFavoritesBinding
import com.example.recipeapp_anton.model.Recipe
import kotlin.getValue

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    val favoriteListListAdapter: RecipesListAdapter = RecipesListAdapter()

    private var _binding: FragmentFavoritesBinding? = null

    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding in null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding
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
        viewModel.loadFavoritesRecipesList()
    }

    private fun setupViews() {
        binding.rvFavorites.adapter = favoriteListListAdapter
    }

    private fun setupListeners() {
        favoriteListListAdapter.setOnItemClickListener { recipeId ->
            openRecipeByRecipeId(recipeId)
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            showOrHideLayout(
                state.isFavoriteListEmpty,
                state.isFavoriteListVisible
            )
            setRecycleViewData(state.recipeList)
        }
    }

    private fun showOrHideLayout(constrainLayoutState: Boolean, rvLayoutState: Boolean) {
        with(binding) {
            constraintlayoutEmptyFavorites.isVisible = constrainLayoutState
            rvFavorites.isVisible = rvLayoutState
        }
    }

    private fun setRecycleViewData(recipeListDataset: List<Recipe>) {
        favoriteListListAdapter.dataSet = recipeListDataset
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(Constants.Bundle.ARG_RECIPE_ID to recipeId)
        findNavController().navigate(R.id.recipeFragment, args = bundle)
    }
}