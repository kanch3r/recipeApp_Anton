package com.example.recipeapp_anton.data

import android.content.Context
import android.util.Log
import com.example.recipeapp_anton.model.Category
import com.example.recipeapp_anton.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.Executors

class RecipesRepository {

    private val threadPool =
        Executors.newFixedThreadPool(Constants.NetworkConstants.THREAD_POOL_SIZE)

    private val prefs = FavoritesSharedPreferences

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = when (Constants.NetworkConstants.LOG_LEVEL) {
            0 -> HttpLoggingInterceptor.Level.NONE
            1 -> HttpLoggingInterceptor.Level.BASIC
            2 -> HttpLoggingInterceptor.Level.HEADERS
            3 -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
    }

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val contentType = "application/json".toMediaType()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    val recipesApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun getCategories(callback: (List<Category>?) -> Unit) {
        threadPool.execute {
            Log.i("!!!", "начал загрузку категорий")
            try {
                val response = recipesApiService.getCategories().execute()
                val result = if (response.isSuccessful) response.body() else null
                callback(result)
                Log.i("!!!", "закончил загрузку категорий. Результат: $result")
            } catch (e: Exception) {
                callback(null)
                Log.i("!!!", "закончил загрузку категорий. Результат: null через Exception")
            }
        }
    }

    fun getRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        threadPool.execute {
            Log.i("!!!", "начал загрузку рецептов")
            try {
                val response = recipesApiService.getRecipesByCategoryId(categoryId).execute()
                val result = if (response.isSuccessful) response.body() else null
                callback(result)
                Log.i("!!!", "закончил загрузку рецептов. Результат: $result")
            } catch (e: Exception) {
                callback(null)
                Log.i("!!!", "закончил загрузку рецептов. Результат: null через Exception")
            }
        }
    }

    fun getRecipeByRecipeId(recipeId: Int, callback: (Recipe?) -> Unit) {
        threadPool.execute {
            Log.i("!!!", "начал загрузку рецепта")
            try {
                val response = recipesApiService.getRecipeByRecipeId(recipeId).execute()
                val result = if (response.isSuccessful) response.body() else null
                callback(result)
                Log.i("!!!", "закончил загрузку рецепта. Результат: $result")
            } catch (e: Exception) {
                callback(null)
                Log.i("!!!", "закончил загрузку рецепта. Результат: null через Exception")
            }
        }
    }

    fun getRecipes(appContext: Context, callback: (List<Recipe>?) -> Unit) {
        threadPool.execute {
            Log.i("!!!", "начал загрузку избранного")
            val favoritesList = prefs.getFavorites(appContext)
                .joinToString(",")

            Log.i("!!!", "ID в избранном: $favoritesList")

            if (favoritesList.isEmpty()) {
                callback(emptyList())
            } else {
                try {
                    val response = recipesApiService.getRecipes(favoritesList).execute()

                    Log.i("!!!", "URL запроса: ${response.raw().request.url}")

                    val result = if (response.isSuccessful) response.body() else null
                    callback(result)
                    Log.i("!!!", "закончил загрузку рецептов по ID. Результат: $result")
                } catch (e: Exception) {
                    callback(null)
                    Log.i(
                        "!!!",
                        "закончил загрузку рецептов по ID. Результат: null через Exception"
                    )
                }
            }
        }
    }
}

