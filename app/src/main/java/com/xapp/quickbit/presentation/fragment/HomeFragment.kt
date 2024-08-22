package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.Meal
import com.xapp.quickbit.data.source.remote.model.RandomMealResponse
import com.xapp.quickbit.databinding.FragmentHomeBinding
import com.xapp.quickbit.presentation.activity.MealDetailesActivity
import com.xapp.quickbit.presentation.activity.MealDetailsActivity
import com.xapp.quickbit.viewModel.DetailsViewModel
import com.xapp.quickbit.viewModel.adapter.HomeAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var detailMvvm: DetailsViewModel
    private lateinit var mostPopularFoodAdapter: HomeAdapter
    private var randomMealId = ""

    companion object {
        const val MEAL_ID = "com.example.easyfood.ui.fragments.idMeal"

        const val MEAL_THUMB = "com.example.easyfood.ui.fragments.thumbMeal"

        const val MEAL_STR = "com.example.easyfood.ui.fragments.strMeal"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailMvvm = ViewModelProvider(this)[DetailsViewModel::class.java]
        binding = FragmentHomeBinding.inflate(layoutInflater)

        mostPopularFoodAdapter = HomeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainFragMVVM = ViewModelProvider(this)[MainFragMVVM::class.java]


        preparePopularMeals()
        onRandomMealClick()
        onRandomLongClick()

        mainFragMVVM.observeRandomMeal().observe(viewLifecycleOwner, Observer { response ->
            val mealImage = view.findViewById<ImageView>(R.id.img_random_meal)
            val imageUrl = response!!.meals[0].strMealThumb
            randomMealId = response.meals[0].idMeal
            Glide.with(this@HomeFragment)
                .load(imageUrl)
                .into(mealImage)
        })

        mostPopularFoodAdapter.setOnClickListener(object : HomeAdapter.OnItemClick {
            override fun onItemClick(meal: Meal) {
                val intent = Intent(activity, MealDetailsActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })

        mostPopularFoodAdapter.setOnLongClickListener(object : HomeAdapter.OnLongItemClick {
            override fun onItemLongClick(meal: Meal) {
                detailMvvm.getMealByIdBottomSheet(meal.idMeal)
            }
        })



        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, randomMealId)
            startActivity(intent)
        }
    }

    private fun onRandomLongClick() {
        binding.randomMeal.setOnLongClickListener {
            detailMvvm.getMealByIdBottomSheet(randomMealId)
            true
        }
    }

    private fun preparePopularMeals() {
        binding.recViewMealsPopular.apply {
            adapter = mostPopularFoodAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        }
    }
}
