package com.example.recipeapp_anton.data.database

import androidx.room.TypeConverter
import com.example.recipeapp_anton.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(ingredientsString: String): List<Ingredient> {
        return Json.decodeFromString(ingredientsString)
    }

    @TypeConverter
    fun fromStringList(strings: List<String>): String {
        return Json.encodeToString(strings)
    }

    @TypeConverter
    fun toStringList(stringsString: String): List<String> {
        return Json.decodeFromString(stringsString)
    }

}