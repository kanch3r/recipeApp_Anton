package com.example.recipeapp_anton.ui.recipes.recipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp_anton.databinding.MethodRecipeBinding

class MethodAdapter(val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(binding: MethodRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.tvMethodDescription
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = MethodRecipeBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method = dataSet[position]
        viewHolder.textView.text = "${position + 1}. $method"
    }

    override fun getItemCount() = dataSet.size
}