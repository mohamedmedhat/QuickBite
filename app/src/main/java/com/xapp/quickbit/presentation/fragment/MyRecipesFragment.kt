package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvMyRecipesRecycleView.visibility = View.GONE

        myRecipesViewModel.allMyCreatedRecipes.observe(viewLifecycleOwner) { recipe ->
            if (!recipe.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.rvMyRecipesRecycleView.visibility = View.VISIBLE
                myRecipesAdapter = MyRecipesAdapter(recipe.toMutableList(), { rec ->
                    goToDetails(rec)
                }, { del ->
                    deleteRecipe(del)
                })
                binding.rvMyRecipesRecycleView.adapter = myRecipesAdapter
            } else {
                binding.tvNoRecipes.visibility = View.VISIBLE
            }
        }

        myRecipesViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.lottieLoading.visibility = View.GONE
            binding.rvMyRecipesRecycleView.visibility = View.GONE
            error?.let {
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e("My Recipes Fragment Error", it)
            }
        }
    }

    private fun goToDetails(recipe: MyRecipesEntity) {
        val bundle = Bundle().apply {
            putParcelable("myCreateRecipe", recipe)
        }
        findNavController().navigate(R.id.action_myRecipesFragment_to_recipeDetailFragment, bundle)
    }


    private fun deleteRecipe(recipe: MyRecipesEntity) {
        myRecipesViewModel.deleteRecipe(recipe)
        myRecipesAdapter.removeItem(recipe)
        CustomNotifications.showSnackBar(
            view = requireView(),
            message = "Item removed successfully",
            duration = Snackbar.LENGTH_LONG,
            action = {}
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}