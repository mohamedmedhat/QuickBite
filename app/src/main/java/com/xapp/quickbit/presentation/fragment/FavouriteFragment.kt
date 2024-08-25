package com.xapp.quickbit.presentation.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity
import com.xapp.quickbit.databinding.FragmentFavouriteBinding
import com.xapp.quickbit.viewModel.FavouriteRecipesViewModel
import com.xapp.quickbit.viewModel.adapter.FavouriteAdapter

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val favouriteRecipesViewModel: FavouriteRecipesViewModel by viewModels()
    private lateinit var adapter: FavouriteAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences(
                "favourite_recipes_details",
                AppCompatActivity.MODE_PRIVATE
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        binding.lottieLoading.visibility = View.VISIBLE
        binding.favRecView.visibility = View.GONE

        favouriteRecipesViewModel.allFavoriteMeals.observe(viewLifecycleOwner) { meals ->
            if (!meals.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.favRecView.visibility = View.VISIBLE
                adapter = FavouriteAdapter(meals.toMutableList(), { meal ->
                    goToDetails(meal)
                }, { meal ->
                    deleteMeal(meal)
                })
                binding.favRecView.adapter = adapter
            } else {
                binding.tvNoRecipes.visibility = View.VISIBLE
            }
        }
    }

    private fun deleteMeal(meal: MealInformationEntity) {
        favouriteRecipesViewModel.deleteMeal(meal)
        adapter.removeItem(meal)
    }

    private fun goToDetails(favourite: MealInformationEntity) {
        val gson = Gson()
        val mealJson = gson.toJson(favourite)

        val edit = sharedPreferences.edit()
        edit.putString("favouriteMealJson", mealJson)
        edit.apply()

        findNavController().navigate(R.id.action_favouriteFragment_to_recipeDetailFragment)
    }

    private fun setupRecyclerView() {
        binding.favRecView.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


