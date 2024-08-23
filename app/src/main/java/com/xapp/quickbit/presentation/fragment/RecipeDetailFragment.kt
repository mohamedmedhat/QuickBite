package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentRecipeDetailBinding
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import com.xapp.quickbit.viewModel.utils.CustomNotifications.showSnackBar

class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        handleOnClick()
    }

    private fun init() {
        val mealDetail = arguments?.getParcelable<MealDetail>("mealDetail")
            ?: arguments?.getParcelable<MealDetail>("searchMealDetail")

        mealDetail?.let { item ->
            binding.apply {
                Glide.with(this@RecipeDetailFragment)
                    .load(item.strMealThumb)
                    .into(ivRecipeDetailImage)
                tvRecipeDetailTitle.text = item.strMeal
                tvRecipeDetailsDescContent.text = item.strInstructions
            }
        } ?: run {
            CustomToast(requireContext(), "Meal detail data is missing", R.drawable.error_24px)
            findNavController().popBackStack()
        }
    }


    private fun handleOnClick() {
        binding.fabBackToHome.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.fabSaveItem.setOnClickListener {
            showSnackBar(
                view = requireView(),
                message = "Item saved successfully",
                duration = Snackbar.LENGTH_LONG,
                actionText = getString(R.string.saved_snackBar),
                action = { findNavController().navigate(R.id.action_recipeDetailFragment_to_favouriteFragment) }
            )
        }

        binding.ivYoutubeRecipe.setOnClickListener {
            val youtubeUrl = arguments?.getParcelable<MealDetail>("mealDetail")?.strYoutube
            youtubeUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                } else {
                    CustomToast(
                        requireContext(),
                        "No app available to open this link",
                        R.drawable.error_24px
                    )
                }
            } ?: CustomToast(requireContext(), "No YouTube link available", R.drawable.error_24px)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
