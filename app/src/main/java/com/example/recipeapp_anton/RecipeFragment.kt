package com.example.recipeapp_anton

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

        binding.rvIngredients.apply {
            addItemDecoration(methodDivider)
            isNestedScrollingEnabled = false
            adapter = ingredientAdapter
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                TODO("Not yet implemented")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }


        })

    }

    private fun initMethodRecycler() {
        val dataSet = recipe?.method ?: return
        val methodAdapter = MethodAdapter(dataSet)
        val methodDivider = createDivider()

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
        binding.tvRecipeTitle.text = recipe?.title
    }
}