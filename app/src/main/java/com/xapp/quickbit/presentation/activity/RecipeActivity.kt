package com.xapp.quickbit.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.ActivityMainBinding
import com.xapp.quickbit.presentation.fragment.AboutFragment
import com.xapp.quickbit.presentation.fragment.FavouriteFragment
import com.xapp.quickbit.presentation.fragment.HomeFragment
import com.xapp.quickbit.presentation.fragment.SearchFragment

public class RecipeActivity : AppCompatActivity() {
    private val bottom=findViewById<BottomNavigationView>(R.id.bottom)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_recipe)
        val homeFragment=HomeFragment()
        val favouriteFragment=FavouriteFragment()
        val searchFragment=SearchFragment()
        val infoFragment=AboutFragment()

        makeCurrentFragment(homeFragment)

    bottom.setOnClickListener{
        when (it.id){
            R.id.home->makeCurrentFragment(homeFragment)
            R.id.favourite->makeCurrentFragment(favouriteFragment)
            R.id.search->makeCurrentFragment(searchFragment)
            R.id.info->makeCurrentFragment(infoFragment)
        }
    }
    }
    private fun makeCurrentFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment,fragment)
            commit()
        }
    }
}