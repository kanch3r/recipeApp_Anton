package com.example.recipeapp_anton

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipeapp_anton.databinding.FragmentRecipeBinding
import models.Recipe

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null

    private var recipe: Recipe? = null

    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding in null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState?.getParcelable(Constants.Bundle.ARG_RECIPE, Recipe::class.java)
        } else {
            savedInstanceState?.getParcelable(Constants.Bundle.ARG_RECIPE)
        }

        binding.testRecipeTitle.text = recipe?.title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}