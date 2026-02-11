package com.example.recipeapp_anton.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipeapp_anton.ui.recipes.recipe.adapter.IngredientAdapter
import com.example.recipeapp_anton.ui.recipes.recipe.adapter.MethodAdapter
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.databinding.FragmentRecipeBinding
import com.example.recipeapp_anton.model.Ingredient
import com.google.android.material.divider.MaterialDividerItemDecoration

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    ) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}

class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    private var _binding: FragmentRecipeBinding? = null

    private val ingredientAdapter: IngredientAdapter = IngredientAdapter()

    private val methodAdapter: MethodAdapter = MethodAdapter()

    private var recipeId: Int? = null

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

        recipeId = requireArguments().getInt(Constants.Bundle.ARG_RECIPE_ID, 0)

        val localRecipeId = recipeId

        if (localRecipeId != null) {
            viewModel.loadRecipe(localRecipeId)
        } else {
            Log.i("Exception", "Recipe ID $recipeId not found")
        }

        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerViews() {
        val divider = createDivider()
        setupIngredients(ingredientAdapter, divider)
        setupMethod(methodAdapter, divider)
    }

    private fun setupRecycleViewData(
        ingredientsDataset: List<Ingredient>,
        methodDataset: List<String>
    ) {
        ingredientAdapter.dataSet = ingredientsDataset
        methodAdapter.dataSet = methodDataset
    }

    private fun setupIngredients(
        ingredientAdapter: IngredientAdapter,
        methodDivider: MaterialDividerItemDecoration
    ) {
        binding.rvIngredients.apply {
            addItemDecoration(methodDivider)
            isNestedScrollingEnabled = false
            adapter = ingredientAdapter
        }
    }

    private fun setupSeekBar(ingredientAdapter: IngredientAdapter) {
        binding.seekBar.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                viewModel.updatePortions(progress)
                ingredientAdapter.updateIngredients(progress)
            }
        )
    }

    private fun setupMethod(
        methodAdapter: MethodAdapter,
        methodDivider: MaterialDividerItemDecoration
    ) {
        binding.rvMethod.apply {
            addItemDecoration(methodDivider)
            isNestedScrollingEnabled = false
            adapter = methodAdapter
        }
    }

    private fun createDivider(): MaterialDividerItemDecoration {
        return MaterialDividerItemDecoration(requireContext(), 1).apply {
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.layout_normal_size)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.layout_normal_size)
            isLastItemDecorated = false
            setDividerColorResource(requireContext(), R.color.divider_color)
        }
    }

    private fun initUI() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            Log.i("HAPPY", "Here is the value of isFavorite variable:${state.isFavorite}")
            setTitleRecipe(state.recipe?.title)
            setFavoriteIcon(state.isFavorite)
            setImageRecipe(state.recipeImage)
            setPortions(state.portions.toString())
            setupRecycleViewData(
                state.recipe?.ingredients ?: emptyList(),
                state.recipe?.method ?: emptyList()
            )
        }

        setupRecyclerViews()
        setupSeekBar(ingredientAdapter)
        setupFavoriteButtonListener(recipeId)
    }

    private fun setImageRecipe(drawable: Drawable?) = binding.ivRecipe.setImageDrawable(drawable)

    private fun setFavoriteIcon(isFavorite: Boolean) {
        val favIcon =
            if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_filled_transparent
        binding.ibFavoriteIcon.setImageResource(favIcon)
    }

    private fun setTitleRecipe(recipeTitle: String?) {
        binding.tvRecipeTitle.text = recipeTitle
    }

    private fun setPortions(portions: String) {
        binding.tvPortionQuantity.text = portions
    }

    private fun setupFavoriteButtonListener(recipeId: Int?) {
        binding.ibFavoriteIcon.setOnClickListener {
            viewModel.onFavoritesClicked(recipeId)
        }
    }
}