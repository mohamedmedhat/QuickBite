package com.xapp.quickbit.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xapp.quickbit.databinding.ActivityMainBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}