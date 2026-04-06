package com.example.recipeapp_anton.ui.recipes.recipesList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp_anton.R
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
            setImageCategory(state.categoryImageUrl)
            setRecycleViewData(state.recipeList)
            state.errorMessage?.let { errorMessage ->
                showToast(errorMessage)
                viewModel.clearErrorMessage()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvRecipes.adapter = recipesListAdapter
    }

    private fun setRecycleViewData(recipeListDataset: List<Recipe>) {
        recipesListAdapter.dataSet = recipeListDataset
    }

    private fun setImageCategory(recipeImage: String?) {
        Glide.with(requireContext())
            .load(recipeImage)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivRecipes)
    }

    private fun setTitleCategory(categoryName: String?) {
        binding.tvCategories.text = categoryName
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}