package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity
import com.xapp.quickbit.databinding.FragmentDashboardBinding
import com.xapp.quickbit.viewModel.MyRecipesViewModel
import com.xapp.quickbit.viewModel.adapter.DashboardAdapter


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardAdapter: DashboardAdapter
    private val myRecipesViewModel: MyRecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        handleDashboardObserving()
        setUpSwipeRefresh()
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvDashboardRecycleView.visibility = View.GONE
    }

    private fun handleDashboardObserving() {
        myRecipesViewModel.allMyCreatedRecipes.observe(viewLifecycleOwner) { recipe ->
            binding.dashboardSwipeRefresh.isRefreshing = false
            if (!recipe.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvDashboardRecycleView.visibility = View.VISIBLE
                dashboardAdapter = DashboardAdapter(recipe.toMutableList()) { rec ->
                    goToDetails(rec)
                }
                binding.rvDashboardRecycleView.adapter = dashboardAdapter
            } else {
                binding.lottieLoading.visibility = View.GONE
                binding.tvNoRecipes.visibility = View.VISIBLE
                binding.dashboardSwipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setUpSwipeRefresh() {
        styleSwipeRefresh()
        binding.dashboardSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.dashboardSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvDashboardRecycleView.visibility = View.GONE
        myRecipesViewModel.allMyCreatedRecipes
    }

    private fun goToDetails(recipe: MyRecipesEntity) {
        val bundle = Bundle().apply {
            putParcelable("dashboardRecipe", recipe)
        }
        findNavController().navigate(R.id.action_dashboardFragment_to_recipeDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}