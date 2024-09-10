package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
        showLoading()
        handleFavouriteObserving()
        handleSwipeRefresh()
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.favRecView.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        adapter = FavouriteAdapter(mutableListOf(), { meal ->
            goToDetails(meal)
        }, { meal ->
            deleteMeal(meal)
        })
        binding.favRecView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favRecView.adapter = adapter
    }

    private fun handleFavouriteObserving() {
        favouriteRecipesViewModel.allFavoriteMeals.observe(viewLifecycleOwner) { meals ->
            binding.savedSwipeRefresh.isRefreshing = false
            if (!meals.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.favRecView.visibility = View.VISIBLE
                binding.tvNoRecipes.visibility = View.GONE
                adapter.updateMeals(meals.toMutableList())
            } else {
                binding.favRecView.visibility = View.GONE
                binding.tvNoRecipes.visibility = View.VISIBLE
                binding.savedSwipeRefresh.isRefreshing = false
            }
        }
    }

    private fun deleteMeal(meal: MealInformationEntity) {
        favouriteRecipesViewModel.deleteMeal(meal)
        adapter.removeItem(meal)
    }

    private fun goToDetails(favourite: MealInformationEntity) {
        val bundle = Bundle().apply {
            putParcelable(KEY_FAVOURITE_MEAL_BUNDLE_KEY, favourite)
        }
        findNavController().navigate(R.id.action_favouriteFragment_to_recipeDetailFragment, bundle)
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.savedSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.savedSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.favRecView.visibility = View.GONE
        favouriteRecipesViewModel.allFavoriteMeals
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_FAVOURITE_MEAL_BUNDLE_KEY = "favouriteMealJson"
    }
}
