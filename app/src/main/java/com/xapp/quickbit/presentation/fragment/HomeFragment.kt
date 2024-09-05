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
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentHomeBinding
import com.xapp.quickbit.viewModel.HomeRecipesViewModel
import com.xapp.quickbit.viewModel.adapter.HomeRecipesAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeRecipesViewModel: HomeRecipesViewModel by viewModels()
    private lateinit var homeRecipesAdapter: HomeRecipesAdapter

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
        setupAdapter()
        handleHomeObserving()
        setupSpinner()
        handleSwipeRefresh()
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvHomeRecycleView.visibility = View.GONE
    }

    private fun setupAdapter() {
        homeRecipesAdapter = HomeRecipesAdapter(ArrayList()) { mealDetail ->
            navigateToItemFragment(mealDetail)
        }
        binding.rvHomeRecycleView.adapter = homeRecipesAdapter

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
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e(ERROR_TAG, it)
            }
        }
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

        val selectedLetter = binding.letterSpinnerFilter.selectedItem.toString()
        homeRecipesViewModel.fetchRecipesByFirstLetter(selectedLetter)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ERROR_TAG = "Home Fragment Error"
        const val HOME_BUNDLE_KEY = "mealDetail"
    }
}


