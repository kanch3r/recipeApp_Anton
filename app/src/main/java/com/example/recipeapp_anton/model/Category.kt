package com.example.recipeapp_anton.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "category_of_recipe")
data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "category_name") val title: String,
    @ColumnInfo(name = "category_description") val description: String,
    @ColumnInfo(name = "category_image") val imageUrl: String
) : Parcelable