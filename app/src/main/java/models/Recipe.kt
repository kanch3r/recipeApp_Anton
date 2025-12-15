package models

data class Recipe(
    val id: Int,
    val title: String,
    val ingredient: List<Ingredient>,
    val method: String,
    val imageUrl: String,
)