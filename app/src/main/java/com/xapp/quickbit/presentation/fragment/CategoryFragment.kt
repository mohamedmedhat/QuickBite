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
import androidx.recyclerview.widget.GridLayoutManager
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Category
import com.xapp.quickbit.databinding.FragmentCategoryBinding
import com.xapp.quickbit.viewModel.CategoryViewModel
import com.xapp.quickbit.viewModel.adapter.CategoryAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryViewModel: CategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        setUpRecycleView()
        handleCategoriesObserving()
        handleSwipeRefresh()
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvCategoriesRecycleView.visibility = View.GONE
    }

    private fun setUpRecycleView() {
        categoryAdapter = CategoryAdapter(ArrayList()) { category ->
            navigateToItemFragment(category)
        }
        setUpGridLayOut()
        binding.rvCategoriesRecycleView.adapter = categoryAdapter
    }

    private fun setUpGridLayOut() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.rvCategoriesRecycleView.layoutManager = gridLayoutManager
    }

    private fun handleCategoriesObserving() {
        categoryViewModel.categoryRecipes.observe(viewLifecycleOwner) { category ->
            binding.categorySwipeRefresh.isRefreshing = false
            if (!category.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvCategoriesRecycleView.visibility = View.VISIBLE
                categoryAdapter.updateData(category)
            }
        }

        categoryViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.categorySwipeRefresh.isRefreshing = false
            binding.rvCategoriesRecycleView.visibility = View.GONE
            binding.tvNoRecipes.visibility = View.VISIBLE
            error?.let {
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e(ERROR_TAG, it)
            }
        }
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.categorySwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.categorySwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvCategoriesRecycleView.visibility = View.GONE
        categoryViewModel.fetchRecipes()
    }


    private fun navigateToItemFragment(category: Category) {
        val bundle = Bundle().apply {
            putParcelable(CATEGORY_MEALS_KEY, category)
        }
        findNavController().navigate(
            R.id.action_categoryFragment_to_categoryDetailsFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ERROR_TAG = "Category Fragment Error"
        const val CATEGORY_MEALS_KEY = "categoryMeals"
    }
}