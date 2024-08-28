package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Category
import com.xapp.quickbit.data.source.remote.model.Meal
import com.xapp.quickbit.databinding.FragmentCategoryDetailsBinding
import com.xapp.quickbit.viewModel.CategoryDetailsViewModel
import com.xapp.quickbit.viewModel.adapter.CategoryDetailsAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications


class CategoryDetailsFragment : Fragment() {
    private var _binding: FragmentCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryDetailsAdapter: CategoryDetailsAdapter
    private val categoryDetailsViewModel: CategoryDetailsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvCategoryDetailsRecycleView.visibility = View.GONE

        val categoryName = arguments?.getParcelable<Category>("categoryMeals")?.strCategory

        categoryDetailsAdapter = CategoryDetailsAdapter(ArrayList()) { meal ->
            goToDetails(meal)
        }
        binding.rvCategoryDetailsRecycleView.adapter = categoryDetailsAdapter

        categoryName?.let {
            categoryDetailsViewModel.getRecipesByCategory(it)
        }

        categoryDetailsViewModel.categoryDetails.observe(viewLifecycleOwner) { category ->
            if (!category.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvCategoryDetailsRecycleView.visibility = View.VISIBLE
                categoryDetailsAdapter.updateData(category)
            }
        }

        categoryDetailsViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.rvCategoryDetailsRecycleView.visibility = View.GONE
            binding.tvNoRecipes.visibility = View.VISIBLE
            error?.let {
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e("CategoryDetails Fragment Error", it)
            }
        }
    }

    private fun goToDetails(meal: Meal) {
        val bundle = Bundle().apply {
            putParcelable("categoryDetailsDetails", meal)
        }
        findNavController().navigate(
            R.id.action_categoryDetailsFragment_to_recipeDetailFragment,
            bundle
        )
    }

}