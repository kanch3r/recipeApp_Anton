package com.example.recipeapp_anton.data

import android.content.Context
import android.util.Log
import com.example.recipeapp_anton.model.Category
import com.example.recipeapp_anton.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RecipesRepository {

    private val prefs = FavoritesSharedPreferences

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = when (Constants.NetworkConstants.LOG_LEVEL) {
            0 -> HttpLoggingInterceptor.Level.NONE
            1 -> HttpLoggingInterceptor.Level.BASIC
            2 -> HttpLoggingInterceptor.Level.HEADERS
            3 -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.ApiConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val recipesApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(): List<Category>? = withContext(Dispatchers.IO) {
        Log.i("!!!", "начал загрузку категорий")
        try {
            val response = recipesApiService.getCategories().execute()
            val result = if (response.isSuccessful) response.body() else null
            Log.i("!!!", "закончил загрузку категорий. Результат: $result")
            result
        } catch (e: Exception) {
            Log.i("!!!", "закончил загрузку категорий. Результат: null через Exception")
            null
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            Log.i("!!!", "начал загрузку рецептов")
            try {
                val response = recipesApiService.getRecipesByCategoryId(categoryId).execute()
                val result = if (response.isSuccessful) response.body() else null
                Log.i("!!!", "закончил загрузку рецептов. Результат: $result")
                result
            } catch (e: Exception) {
                Log.i("!!!", "закончил загрузку рецептов. Результат: null через Exception")
                null
            }
        }

    suspend fun getRecipeByRecipeId(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        Log.i("!!!", "начал загрузку рецепта")
        try {
            val response = recipesApiService.getRecipeByRecipeId(recipeId).execute()
            val result = if (response.isSuccessful) response.body() else null
            Log.i("!!!", "закончил загрузку рецепта. Результат: $result")
            result
        } catch (e: Exception) {
            Log.i("!!!", "закончил загрузку рецепта. Результат: null через Exception")
            null
        }
    }

    suspend fun getRecipes(appContext: Context): List<Recipe>? = withContext(Dispatchers.IO) {
        Log.i("!!!", "начал загрузку избранного")
        val favoritesList = prefs.getFavorites(appContext).joinToString(",")
        Log.i("!!!", "ID в избранном: $favoritesList")

        if (favoritesList.isEmpty()) {
            emptyList()
        } else {
            try {
                val response = recipesApiService.getRecipes(favoritesList).execute()
                val result = if (response.isSuccessful) response.body() else null
                Log.i("!!!", "закончил загрузку рецептов по ID. Результат: $result")
                result
            } catch (e: Exception) {
                Log.i(
                    "!!!",
                    "закончил загрузку рецептов по ID. Результат: null через Exception"
                )
                null
            }
        }
    }
}

