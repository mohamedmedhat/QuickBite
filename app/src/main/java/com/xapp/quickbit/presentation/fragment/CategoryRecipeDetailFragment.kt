package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.Meal
import com.xapp.quickbit.databinding.FragmentCategoryRecipeDetailBinding
import com.xapp.quickbit.presentation.fragment.CategoryDetailsFragment.Companion.CATEGORY_DETAILS_DETAILS_KEY
import com.xapp.quickbit.viewModel.utils.CustomNotifications

class CategoryRecipeDetailFragment : Fragment() {
    private var _binding: FragmentCategoryRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private var categoryDetail: Meal? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleData()
        showCategoryDetails()
        handleSwipeRefresh()
    }

    private fun getBundleData() {
        categoryDetail = arguments?.getParcelable(CATEGORY_DETAILS_DETAILS_KEY)
    }

    private fun showCategoryDetails() {
        binding.categoryRecipeDetailSwipeRefresh.isRefreshing = false

        categoryDetail?.let { detail ->
            binding.apply {
                Glide.with(this@CategoryRecipeDetailFragment)
                    .load(detail.strMealThumb)
                    .placeholder(R.drawable.images_placeholder)
                    .error(R.drawable.error_24px)
                    .into(ivRecipeDetailImage)
                tvRecipeDetailTitle.text = detail.strMeal
            }
        } ?: CustomNotifications.CustomToast(
            requireContext(),
            ContextCompat.getString(requireContext(), R.string.failed_get_data_msg),
            R.drawable.error_24px
        )
    }


    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.categoryRecipeDetailSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.categoryRecipeDetailSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        showCategoryDetails()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}