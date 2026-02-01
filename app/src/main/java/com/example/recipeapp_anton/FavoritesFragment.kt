package com.example.recipeapp_anton

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp_anton.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
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
        val favoritesList = FavoritesSharedPreferences.getFavorites(requireContext())
            .mapNotNull { it.toIntOrNull() }.toSet()

        if (favoritesList.isEmpty()) {
            setEmptyState()
        } else {
            initRecycler(favoritesList)
        }
    }

    private fun setEmptyState() {
        binding.constraintlayoutEmptyFavorites.isVisible = true
        binding.rvFavorites.isVisible = false
    }

    private fun initRecycler(favorites: Set<Int>) {
        val favoriteRecipes = STUB.getRecipesByIds(favorites)
        val favoriteListListAdapter = RecipesListAdapter(favoriteRecipes)
        binding.rvFavorites.adapter = favoriteListListAdapter

        favoriteListListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        Log.i("Выбор рецепта", "Пользователь выбрал рецепт: ${recipe?.title}")

        val bundle = bundleOf(Constants.Bundle.ARG_RECIPE to recipe)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }
}