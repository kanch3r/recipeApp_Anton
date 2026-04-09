package com.example.recipeapp_anton.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM category_of_recipe")
    suspend fun getAllCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(categories: List<Category>)
}