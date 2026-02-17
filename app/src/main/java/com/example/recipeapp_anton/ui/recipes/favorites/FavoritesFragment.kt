package com.example.recipeapp_anton.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.ui.recipes.recipesList.adapter.RecipesListAdapter
import com.example.recipeapp_anton.databinding.FragmentFavoritesBinding
import com.example.recipeapp_anton.ui.recipes.recipe.RecipeFragment
import kotlin.getValue

class RecipeItemClickListener(val clickOnItem: (Int) -> Unit) :
    RecipesListAdapter.OnItemClickListener {
    override fun onItemClick(recipeId: Int) {
        clickOnItem(recipeId)
    }
}

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
        setupFavoriteScreenView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupFavoriteScreenView() {

        viewModel.loadFavoritesRecipesList()

        binding.rvFavorites.adapter = favoriteListListAdapter

        favoriteListListAdapter.setOnItemClickListener(
            RecipeItemClickListener { recipeId ->
                openRecipeByRecipeId(recipeId)
            })

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.constraintlayoutEmptyFavorites.isVisible = state.isFavoriteListEmpty
            binding.rvFavorites.isVisible = state.isFavoriteListVisible
            favoriteListListAdapter.dataSet = state.recipeList
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = bundleOf(Constants.Bundle.ARG_RECIPE_ID to recipeId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }
}