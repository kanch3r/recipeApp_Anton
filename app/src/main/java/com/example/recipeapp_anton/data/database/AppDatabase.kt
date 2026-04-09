package com.example.recipeapp_anton.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipeapp_anton.data.Constants
import com.example.recipeapp_anton.model.CategoriesDao
import com.example.recipeapp_anton.model.Category
import com.example.recipeapp_anton.model.Recipe
import com.example.recipeapp_anton.model.RecipesDao

@Database(
    entities = [Category::class, Recipe::class],
    version = Constants.DatabaseConstants.DATABASE_VERSION
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}