package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Meal
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentHomeBinding
import com.xapp.quickbit.viewModel.HomeAreaViewModel
import com.xapp.quickbit.viewModel.HomeRecipesViewModel
import com.xapp.quickbit.viewModel.RecipeDetailsViewModel
import com.xapp.quickbit.viewModel.adapter.HomeAreaAdapter
import com.xapp.quickbit.viewModel.adapter.HomeAreaResultAdapter
import com.xapp.quickbit.viewModel.adapter.HomeRecipesAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeRecipesViewModel: HomeRecipesViewModel by viewModels()
    private val homeAreaViewModel: HomeAreaViewModel by viewModels()
    private val recipeDetailsViewModel: RecipeDetailsViewModel by viewModels()

    private lateinit var homeRecipesAdapter: HomeRecipesAdapter
    private lateinit var homeAreaAdapter: HomeAreaAdapter
    private lateinit var homeAreaResultAdapter: HomeAreaResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        homeRecipesViewModel = ViewModelProvider(this)[HomeRecipesViewModel::class.java]  => old way

        showLoading()
        setupAdapters()
        handleHomeAreaObserving()
        handleHomeAreaResultObserving()
        handleHomeObserving()
        setupSpinner()
        handleSwipeRefresh()
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvHomeRecycleView.visibility = View.GONE
        binding.rvAreaRecycleView.visibility = View.GONE
        binding.rvAreaResultRecycleView.visibility = View.GONE
    }

    private fun setupAdapters() {
        homeRecipesAdapter = HomeRecipesAdapter(ArrayList()) { mealDetail ->
            navigateToItemFragment(mealDetail)
        }
        binding.rvHomeRecycleView.adapter = homeRecipesAdapter

        homeAreaAdapter = HomeAreaAdapter(ArrayList()) { area ->
            showRecipesByArea(area.strArea)
        }
        binding.rvAreaRecycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvAreaRecycleView.adapter = homeAreaAdapter

        homeAreaResultAdapter = HomeAreaResultAdapter(ArrayList()) { meal ->
            navigateToDetails(meal)
        }
        binding.rvAreaResultRecycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvAreaResultRecycleView.adapter = homeAreaResultAdapter
    }


    private fun handleHomeObserving() {
        homeRecipesViewModel.mealRecipes.observe(viewLifecycleOwner) { recipes ->
            binding.homeSwipeRefresh.isRefreshing = false

            if (recipes.isNotEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvHomeRecycleView.visibility = View.VISIBLE
                homeRecipesAdapter.updateData(recipes)
            }
        }

        homeRecipesViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            binding.homeSwipeRefresh.isRefreshing = false

            binding.lottieLoading.visibility = View.GONE
            binding.rvHomeRecycleView.visibility = View.GONE
            binding.tvNoRecipes.visibility = View.VISIBLE

            errorMessage?.let {
                handleError(it)
            }
        }
    }

    private fun handleHomeAreaObserving() {
        homeAreaViewModel.area.observe(viewLifecycleOwner) { area ->
            binding.homeSwipeRefresh.isRefreshing = false
            if (!area.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvAreaRecycleView.visibility = View.VISIBLE
                homeAreaAdapter.updateData(area)
            }
        }

        homeAreaViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.homeSwipeRefresh.isRefreshing = false

            binding.lottieLoading.visibility = View.GONE
            binding.rvAreaRecycleView.visibility = View.GONE
            binding.tvNoRecipes.visibility = View.VISIBLE

            error?.let {
                handleError(it)
            }
        }
    }

    private fun handleHomeAreaResultObserving() {
        homeAreaViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            binding.homeSwipeRefresh.isRefreshing = false
            if (!recipe.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvAreaResultRecycleView.visibility = View.VISIBLE
                homeAreaResultAdapter.updateData(recipe)
            }
        }

        homeAreaViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.homeSwipeRefresh.isRefreshing = false

            binding.lottieLoading.visibility = View.GONE
            binding.rvAreaResultRecycleView.visibility = View.GONE
            binding.tvNoRecipes.visibility = View.VISIBLE

            error?.let {
                handleError(it)
            }
        }
    }

    private fun showRecipesByArea(area: String) {
        homeAreaViewModel.getRecipesByArea(area)
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.homeSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvHomeRecycleView.visibility = View.GONE
        binding.rvAreaRecycleView.visibility = View.GONE
        binding.rvAreaResultRecycleView.visibility = View.GONE

        val selectedLetter = binding.letterSpinnerFilter.selectedItem.toString()
        homeRecipesViewModel.fetchRecipesByFirstLetter(selectedLetter)
        homeAreaViewModel.getAllArea()
        homeAreaViewModel.getRecipesByArea()
    }

    private fun styleSwipeRefresh() {
        binding.homeSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun navigateToItemFragment(mealDetail: MealDetail) {
        val bundle = Bundle().apply {
            putParcelable(HOME_BUNDLE_KEY, mealDetail)
        }
        findNavController().navigate(R.id.action_homeFragment_to_recipeDetailFragment, bundle)
    }

    private fun navigateToDetails(meal: Meal) {
        recipeDetailsViewModel.getMealDetailsById(meal.idMeal) { mealDetail ->
            if (mealDetail != null) {
                val bundle = Bundle().apply {
                    putParcelable(HOME_AREA_BUNDLE_KEY, mealDetail)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_recipeDetailFragment,
                    bundle
                )
            }
        }
    }


    private fun setupSpinner() {
        val letters = resources.getStringArray(R.array.letters_array)
        val spinnerAdapter = object :
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, letters) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view as TextView
                textView.setTextColor(resources.getColor(R.color.darkGreen, null))
                textView.setBackgroundColor(
                    resources.getColor(
                        R.color.white,
                        null
                    )
                )
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView
                if (position == binding.letterSpinnerFilter.selectedItemPosition) {
                    textView.setTextColor(resources.getColor(R.color.white, null))
                    textView.setBackgroundColor(resources.getColor(R.color.darkGreen, null))
                } else {
                    textView.setTextColor(resources.getColor(R.color.darkGreen, null))
                    textView.setBackgroundColor(resources.getColor(R.color.white, null))
                }
                return view
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.letterSpinnerFilter.adapter = spinnerAdapter

        binding.letterSpinnerFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedLetter = letters[position]
                    homeRecipesViewModel.fetchRecipesByFirstLetter(selectedLetter)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected if needed
                }
            }
    }

    private fun handleError(message: String) {
        CustomNotifications.CustomToast(requireContext(), message, R.drawable.error_24px)
        binding.tvNoRecipes.visibility = View.VISIBLE
        Log.e(ERROR_TAG, message)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ERROR_TAG = "Home Fragment Error"
        const val HOME_BUNDLE_KEY = "mealDetail"
        const val HOME_AREA_BUNDLE_KEY = "area_meals"
    }
}


