package com.xapp.quickbit.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
import com.bumptech.glide.gifdecoder.R
import com.xapp.quickbit.databinding.ActivityMainBinding
=======
import androidx.activity.enableEdgeToEdge
import com.xapp.quickbit.databinding.ActivityAuthBinding
>>>>>>> 59c289dd795c237dd9bdc9fe70d59d2079ab864e

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
<<<<<<< HEAD
        binding.btn1.setOnClickListener{
            val intent=Intent(this,RecipeActivity::class.java)
            startActivity(intent)

        }
=======
        enableEdgeToEdge()
>>>>>>> 59c289dd795c237dd9bdc9fe70d59d2079ab864e
    }
}
