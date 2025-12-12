package com.example.recipeapp_anton

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapp_anton.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    var _binding: FragmentListCategoriesBinding? = null
    val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding in null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}