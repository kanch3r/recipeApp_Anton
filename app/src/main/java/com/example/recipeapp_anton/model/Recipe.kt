package com.example.recipeapp_anton.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "category_id_to_recipe_belong") val categoryId: Int? = null,
    @ColumnInfo(name = "recipe_name") val title: String,
    @ColumnInfo(name = "recipe_ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo(name = "cooking_method") val method: List<String>,
    @ColumnInfo(name = "recipe_image") val imageUrl: String,
    @ColumnInfo(name = "favorite_list_status") val isFavorite: Boolean = false,
)