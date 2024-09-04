package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Category
import com.xapp.quickbit.data.source.remote.model.Meal
import com.xapp.quickbit.databinding.FragmentCategoryDetailsBinding
import com.xapp.quickbit.presentation.fragment.CategoryFragment.Companion.CATEGORY_MEALS_KEY
import com.xapp.quickbit.viewModel.CategoryDetailsViewModel
import com.xapp.quickbit.viewModel.adapter.CategoryDetailsAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications


class CategoryDetailsFragment : Fragment() {
    private var _binding: FragmentCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryDetailsAdapter: CategoryDetailsAdapter

    private val categoryDetailsViewModel: CategoryDetailsViewModel by viewModels()
    private var categoryName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleData()
        showLoading()
        setUpRecycleView()
        showSelectedCategoryItems()
        handleCategoryDetailsObserving()
        handleSwipeRefresh()
    }

    private fun getBundleData() {
        categoryName = arguments?.getParcelable<Category>(CATEGORY_MEALS_KEY)?.strCategory
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvCategoryDetailsRecycleView.visibility = View.GONE
    }

    private fun setUpRecycleView() {
        categoryDetailsAdapter = CategoryDetailsAdapter(ArrayList()) { meal ->
            goToDetails(meal)
        }
        binding.rvCategoryDetailsRecycleView.adapter = categoryDetailsAdapter
    }

    private fun showSelectedCategoryItems() {
        categoryName?.let {
            categoryDetailsViewModel.getRecipesByCategory(it)
        }
    }

    private fun handleCategoryDetailsObserving() {
        categoryDetailsViewModel.categoryDetails.observe(viewLifecycleOwner) { category ->
            binding.categoryDetailsSwipeRefresh.isRefreshing = false
            if (!category.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvCategoryDetailsRecycleView.visibility = View.VISIBLE
                categoryDetailsAdapter.updateData(category)
            }
        }

        categoryDetailsViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.categoryDetailsSwipeRefresh.isRefreshing = false
            binding.rvCategoryDetailsRecycleView.visibility = View.GONE
            binding.tvNoRecipes.visibility = View.VISIBLE
            error?.let {
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e("CategoryDetails Fragment Error", it)
            }
        }
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.categoryDetailsSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.categoryDetailsSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvCategoryDetailsRecycleView.visibility = View.GONE
        categoryName?.let { categoryDetailsViewModel.getRecipesByCategory(it) }
    }

    private fun goToDetails(meal: Meal) {
        val bundle = Bundle().apply {
            putParcelable(CATEGORY_DETAILS_DETAILS_KEY, meal)
        }
        findNavController().navigate(
            R.id.action_categoryDetailsFragment_to_recipeDetailFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CATEGORY_DETAILS_DETAILS_KEY = "categoryDetailsDetails"
    }
}