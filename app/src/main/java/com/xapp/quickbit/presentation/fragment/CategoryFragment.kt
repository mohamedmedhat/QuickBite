package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var categoryViewModel: CategoryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvCategoriesRecycleView.visibility = View.GONE

        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.rvCategoriesRecycleView.layoutManager = gridLayoutManager

        categoryAdapter = CategoryAdapter(ArrayList()) { category ->
            navigateToItemFragment(category)
        }
        binding.rvCategoriesRecycleView.adapter = categoryAdapter

        categoryViewModel.categoryRecipes.observe(viewLifecycleOwner) { category ->
            if (!category.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvCategoriesRecycleView.visibility = View.VISIBLE
                categoryAdapter.updateData(category)
            }
        }

        categoryViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.rvCategoriesRecycleView.visibility = View.GONE
            binding.tvNoRecipes.visibility = View.VISIBLE
            error?.let {
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e("Category Fragment Error", it)
            }
        }

    }


    private fun navigateToItemFragment(category: Category) {
        val bundle = Bundle().apply {
            putParcelable("categoryMeals", category)
        }
        findNavController().navigate(
            R.id.action_categoryFragment_to_categoryDetailsFragment,
            bundle
        )
    }

}