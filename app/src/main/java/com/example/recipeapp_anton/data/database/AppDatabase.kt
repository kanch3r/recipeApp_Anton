package com.example.recipeapp_anton.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp_anton.model.CategoriesDao
import com.example.recipeapp_anton.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriesDao(): CategoriesDao
}