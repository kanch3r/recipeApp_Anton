package com.example.recipeapp_anton.data

import com.example.recipeapp_anton.model.Category
import com.example.recipeapp_anton.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: Int): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipeByRecipeId(@Path("id") id: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipes(@Query("ids") ids: String): Call<List<Recipe>>
}