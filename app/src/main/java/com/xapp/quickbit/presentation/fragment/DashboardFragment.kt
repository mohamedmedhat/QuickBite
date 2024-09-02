package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvDashboardRecycleView.visibility = View.GONE

        myRecipesViewModel.allMyCreatedRecipes.observe(viewLifecycleOwner) { recipe ->
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
            }
        }
    }

    private fun goToDetails(recipe: MyRecipesEntity) {
        val bundle = Bundle().apply {
            putParcelable("dashboardRecipe", recipe)
        }
        findNavController().navigate(R.id.action_dashboardFragment_to_recipeDetailFragment, bundle)
    }

}