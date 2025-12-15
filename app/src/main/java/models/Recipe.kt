package models

data class Recipe(
    val id: Int,
    val title: String,
    val ingredient: Ingredient,
)