package com.example.recipeapp_anton.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes WHERE category_id_to_recipe_belong = :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeFromCacheById(recipeId: Int): Recipe?

    @Query("SELECT * FROM recipes WHERE favorite_list_status = 1")
    suspend fun getFavoritesRecipes(): List<Recipe>

    @Query("UPDATE recipes SET favorite_list_status =:isFavorite WHERE id =:recipeId ")
    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)
}