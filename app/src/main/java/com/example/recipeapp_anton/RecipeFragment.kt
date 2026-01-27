package com.example.recipeapp_anton

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.recipeapp_anton.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import models.Recipe
import kotlin.with

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
            requireArguments().getParcelable(Constants.Bundle.ARG_RECIPE, Recipe::class.java)
        } else {
            requireArguments().getParcelable(Constants.Bundle.ARG_RECIPE)
        }

        initUI(view)
        initIngredientRecycler()
        initMethodRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initIngredientRecycler() {
        val dataSet = recipe?.ingredients ?: return
        val ingredientAdapter = IngredientAdapter(dataSet)
        val methodDivider = createDivider()

        setupIngredients(ingredientAdapter, methodDivider)
        setupSeekBar(ingredientAdapter)
    }

    private fun initMethodRecycler() {
        val dataSet = recipe?.method ?: return
        val methodAdapter = MethodAdapter(dataSet)
        val methodDivider = createDivider()

        setupMethod(methodAdapter, methodDivider)
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
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                binding.tvPortionQuantity.text = progress.toString()
                ingredientAdapter.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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

    private fun initUI(view: View) {
        setImageRecipe(view)
        setupFavoriteIconButton()
        setTitleRecipe()
    }

    private fun setImageRecipe(view: View) {
        val localImageUrl = recipe?.imageUrl
        val drawable = if (localImageUrl != null) {
            try {
                view.context.assets
                    .open(localImageUrl)
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            } catch (e: Exception) {
                Log.i("catch exception", "Image not found: ${recipe?.imageUrl}")
                null
            }
        } else null
        binding.ivRecipe.setImageDrawable(drawable)
    }

    private fun setupFavoriteIconButton() {
        val recipeId: Int = recipe?.id ?: run {
            binding.ibFavoriteIcon.isEnabled = false
            Log.i("catch exception", "Отсутствует ID рецепта. Кнопка отключена")
            return
        }

        updateFavoriteIcon(recipeId)

        binding.ibFavoriteIcon.setOnClickListener {
            val favoriteList = getFavorites()

            if (favoriteList.contains(recipeId.toString())) {
                favoriteList.remove(recipeId.toString())
                Log.i("SP", "Рецепт $recipeId удалён из избранного")
            } else {
                favoriteList.add(recipeId.toString())
                Log.i("SP", "Рецепт $recipeId добавлен в избранное")
            }

            saveFavorite(favoriteList)
            updateFavoriteIcon(recipeId)
        }
    }

    private fun updateFavoriteIcon(recipeId: Int) {
        val favoriteList = getFavorites()
        val favIcon = if (favoriteList.contains(recipeId.toString())) {
            R.drawable.ic_heart_filled
        } else {
            R.drawable.ic_heart_filled_transparent
        }
        binding.ibFavoriteIcon.setImageResource(favIcon)
    }

    private fun setTitleRecipe() {
        binding.tvRecipeTitle.text = recipe?.title
    }

    private fun saveFavorite(favoriteIds: Set<String>) {
        val sharedPref = requireContext().getSharedPreferences(
            Constants.PREFERENCE_FAVORITE_FILE,
            Context.MODE_PRIVATE
        )
        sharedPref.edit()
            .putStringSet(Constants.PREFERENCE_FAVORITE_RECIPE, favoriteIds)
            .apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPref = requireContext().getSharedPreferences(
            Constants.PREFERENCE_FAVORITE_FILE,
            Context.MODE_PRIVATE
        )
        val currentDataSet = sharedPref.getStringSet(
            Constants.PREFERENCE_FAVORITE_RECIPE,
            emptySet<String>()
        )
        val newDataSet = HashSet<String>(currentDataSet)
        return newDataSet
    }
}