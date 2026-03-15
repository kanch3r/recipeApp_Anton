package com.example.recipeapp_anton.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipeapp_anton.R
import com.example.recipeapp_anton.databinding.ActivityMainBinding
import com.example.recipeapp_anton.model.Category
import com.example.recipeapp_anton.model.Recipe
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        window.navigationBarColor = ContextCompat.getColor(
            this, R.color.main_background_color
        )

        threadPool.execute {
            loadCategories()
        }

        Log.i("!!!", "Метод OnCreate выполняется на потоке: ${Thread.currentThread().name}")

        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavorite.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }
    }

    private fun loadCategories() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = when (3) {
                0 -> HttpLoggingInterceptor.Level.NONE
                1 -> HttpLoggingInterceptor.Level.BASIC
                2 -> HttpLoggingInterceptor.Level.HEADERS
                3 -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val request: Request = Request.Builder()
            .url("https://recipes.androidsprint.ru/api/category")
            .build()

        client.newCall(request).execute().use { response ->

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "responseCode: ${response.code}")
            Log.i("!!!", "responseMessage: ${response.message}")

            val gson = Gson()
            val jsonBody = response.body?.string()
            val categories = gson.fromJson(jsonBody, Array<Category>::class.java)
            categories.forEach { category ->
                Log.i("!!!", "id: ${category.id}")
                Log.i("!!!", "title: ${category.title}")
                Log.d("!!!", "description: ${category.description}")
                Log.d("!!!", "imageUrl: ${category.imageUrl}")
            }

            val categoryIds = categories.map { it.id }

            categoryIds.forEach { categoryId ->

                val request: Request = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category/${categoryId}/recipes")
                    .build()

                client.newCall(request).execute().use { response ->
                    val jsonBody = response.body?.string()
                    val categoryRecipes =
                        gson.fromJson(jsonBody, Array<Recipe>::class.java)
                    categoryRecipes.forEach { recipe ->
                        Log.i("!!!", "id: ${recipe.id}")
                        Log.i("!!!", "title: ${recipe.title}")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}