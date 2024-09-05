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
import com.google.android.material.snackbar.Snackbar
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity
import com.xapp.quickbit.databinding.FragmentMyRecipesBinding
import com.xapp.quickbit.viewModel.MyRecipesViewModel
import com.xapp.quickbit.viewModel.adapter.MyRecipesAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications


class MyRecipesFragment : Fragment() {
    private var _binding: FragmentMyRecipesBinding? = null
    val binding get() = _binding!!

    private lateinit var myRecipesAdapter: MyRecipesAdapter
    private val myRecipesViewModel: MyRecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        handleMyRecipesObserving()
        handleSwipeRefresh()
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvMyRecipesRecycleView.visibility = View.GONE
    }

    private fun setUpRecycleView(recipe: List<MyRecipesEntity>) {
        myRecipesAdapter = MyRecipesAdapter(recipe.toMutableList(), { rec ->
            goToDetails(rec)
        }, { del ->
            deleteRecipe(del)
        })
        binding.rvMyRecipesRecycleView.adapter = myRecipesAdapter
    }

    private fun handleMyRecipesObserving() {
        myRecipesViewModel.allMyCreatedRecipes.observe(viewLifecycleOwner) { recipe ->
            binding.myRecipesSwipeRefresh.isRefreshing = false
            if (!recipe.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvMyRecipesRecycleView.visibility = View.VISIBLE
                setUpRecycleView(recipe)
            } else {
                binding.tvNoRecipes.visibility = View.VISIBLE
                binding.myRecipesSwipeRefresh.isRefreshing = false
            }
        }

        myRecipesViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.myRecipesSwipeRefresh.isRefreshing = false
            binding.lottieLoading.visibility = View.GONE
            binding.rvMyRecipesRecycleView.visibility = View.GONE
            error?.let {
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e(ERROR_TAG, it)
            }
        }
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.myRecipesSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.myRecipesSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvMyRecipesRecycleView.visibility = View.GONE
        myRecipesViewModel.allMyCreatedRecipes
    }

    private fun goToDetails(recipe: MyRecipesEntity) {
        val bundle = Bundle().apply {
            putParcelable(CREATED_RECIPE_BUNDLE_KEY, recipe)
        }
        findNavController().navigate(R.id.action_myRecipesFragment_to_recipeDetailFragment, bundle)
    }


    private fun deleteRecipe(recipe: MyRecipesEntity) {
        myRecipesViewModel.deleteRecipe(recipe)
        myRecipesAdapter.removeItem(recipe)
        CustomNotifications.showSnackBar(
            view = requireView(),
            message = ContextCompat.getString(
                requireContext(),
                R.string.item_removed_successfully_msg
            ),
            duration = Snackbar.LENGTH_LONG,
            action = {}
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ERROR_TAG = "My Recipes Fragment Error"
        const val CREATED_RECIPE_BUNDLE_KEY = "myCreateRecipe"
    }

}