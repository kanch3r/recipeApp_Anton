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

        val thread = Thread({
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                val jsonBody = connection.inputStream.bufferedReader().readText()
                val gson = Gson()
                val categories = gson.fromJson(jsonBody, Array<Category>::class.java)

                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                Log.i("!!!", "responseCode: ${connection.responseCode}")
                Log.i("!!!", "responseMessage: ${connection.responseMessage}")

                categories.forEach { category ->
                    Log.i("!!!", "id: ${category.id}")
                    Log.i("!!!", "title: ${category.title}")
                    Log.d("!!!", "description: ${category.description}")
                    Log.d("!!!", "imageUrl: ${category.imageUrl}")
                }

                val categoryIds = categories.map { it.id }
                categoryIds.forEach { categoryId ->
                    threadPool.execute {
                        Log.i("!!!", "--начало threadPool: ${Thread.currentThread().name}")

                        val url =
                            URL("https://recipes.androidsprint.ru/api/category/${categoryId}/recipes")
                        val connection = url.openConnection() as HttpURLConnection
                        try {
                            connection.connect()
                            val jsonBody = connection.inputStream.bufferedReader().readText()
                            val gson = Gson()
                            val categoryRecipes = gson.fromJson(jsonBody, Array<Recipe>::class.java)

                            categoryRecipes.forEach { recipe ->
                                Log.i("!!!", "id: ${recipe.id}")
                                Log.i("!!!", "title: ${recipe.title}")
                            }

                            Log.i("!!!", "--окончание threadPool: ${Thread.currentThread().name}")
                        } catch (e: Exception) {
                            Log.e("!!!", "Ошибка: ${e.message}")
                        } finally {
                            connection.disconnect()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }, "поток загрузки экрана Категории")

        thread.start()

        Log.i("!!!", "Метод OnCreate выполняется на потоке: ${Thread.currentThread().name}")

        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavorite.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }
    }
}