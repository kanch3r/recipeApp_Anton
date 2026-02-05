package com.example.recipeapp_anton.data

import android.content.Context
import com.example.recipeapp_anton.data.Constants

object FavoritesSharedPreferences {

    fun saveFavorite(context: Context, favoriteIds: Set<String>) {
        val sharedPref = context.getSharedPreferences(
            Constants.PREFERENCE_FAVORITE_FILE,
            Context.MODE_PRIVATE
        )
        sharedPref.edit()
            .putStringSet(Constants.PREFERENCE_FAVORITE_RECIPE, favoriteIds)
            .apply()
    }

    fun getFavorites(context: Context): MutableSet<String> {
        val sharedPref = context.getSharedPreferences(
            Constants.PREFERENCE_FAVORITE_FILE,
            Context.MODE_PRIVATE
        )
        val currentDataSet = sharedPref.getStringSet(
            Constants.PREFERENCE_FAVORITE_RECIPE,
            emptySet<String>()
        )
        val newDataSet = HashSet<String>(currentDataSet)
        return newDataSet
    }
}