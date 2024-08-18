package com.xapp.quickbit.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.xapp.quickbit.databinding.ActivityRecipeActivityBinding

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeActivityBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
    }
}