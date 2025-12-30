package com.example.recipeapp_anton

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp_anton.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

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

        categoryId = requireArguments().getInt(Constants.Bundle.ARG_CATEGORY_ID, 0)
        categoryName = requireArguments().getString(Constants.Bundle.ARG_CATEGORY_NAME)
        categoryImageUrl = requireArguments().getString(Constants.Bundle.ARG_CATEGORY_IMAGE_URL)

        initRecycler(categoryId)
        binding.tvCategories.text = categoryName

        val localImageUrl = categoryImageUrl
        val drawable = if (localImageUrl != null) {
            try {
                view.context.assets
                    .open(localImageUrl)
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            } catch (e: Exception) {
                Log.i("catch exception", "Image not found: $categoryImageUrl")
                null
            }
        } else null

        binding.ivRecipes.setImageDrawable(drawable)

        Log.i(
            "Результат передачи",
            "Открываем рецепты категории: ${categoryName ?: "Неизвестная"}"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(categoryId: Int?) {
        val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        binding.rvRecipes.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
                Log.i("Выбор категории", "Пользователь выбрал: $recipeId")
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer)
        }
    }
}