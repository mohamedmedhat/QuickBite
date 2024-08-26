package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentRecipeDetailBinding
import com.xapp.quickbit.viewModel.FavouriteRecipesViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import com.xapp.quickbit.viewModel.utils.CustomNotifications.showSnackBar

class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val favouriteRecipesViewModel: FavouriteRecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            requireContext().getSharedPreferences(
                "favourite_recipes_details",
                AppCompatActivity.MODE_PRIVATE
            )
    }


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
        val gson = Gson()
        val mealJson = sharedPreferences.getString("favouriteMealJson", null)
        val mealFromPrefs = gson.fromJson(mealJson, MealInformationEntity::class.java)

        val mealDetail = arguments?.getParcelable<MealDetail>("mealDetail")
            ?: arguments?.getParcelable("searchMealDetail")

        mealDetail?.let { item ->
            binding.apply {
                Glide.with(this@RecipeDetailFragment)
                    .load(item.strMealThumb)
                    .placeholder(R.drawable.images_placeholder)
                    .error(R.drawable.error_24px)
                    .into(ivRecipeDetailImage)
                tvRecipeDetailTitle.text = item.strMeal
                tvRecipeDetailsDescContent.text = item.strInstructions
                tvCategoryIconText.text = item.strCategory
                tvLocationIconText.text = item.strArea
            }
        } ?: run {
            mealFromPrefs?.let { meal ->
                binding.apply {
                    Glide.with(this@RecipeDetailFragment)
                        .load(meal.mealThumb)
                        .placeholder(R.drawable.images_placeholder)
                        .error(R.drawable.error_24px)
                        .into(ivRecipeDetailImage)
                    tvRecipeDetailTitle.text = meal.mealName
                    tvRecipeDetailsDescContent.text = meal.mealInstruction
                    tvCategoryIconText.text = meal.mealCategory
                    tvLocationIconText.text = meal.mealCountry
                }
            } ?: run {
                CustomToast(requireContext(), "Meal detail data is missing", R.drawable.error_24px)
                findNavController().popBackStack()
            }
        }
    }


    private fun handleOnClick() {
        binding.fabBackToHome.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.fabSaveItem.setOnClickListener {
            val mealDetail = arguments?.getParcelable<MealDetail>("mealDetail")
            mealDetail?.let { meal ->
                val mealEntity = MealInformationEntity(
                    mealId = meal.idMeal,
                    mealName = meal.strMeal,
                    mealInstruction = meal.strInstructions,
                    mealThumb = meal.strMealThumb,
                    mealCategory = meal.strCategory,
                    mealCountry = meal.strArea,
                    mealYoutubeLink = meal.strYoutube
                )
                favouriteRecipesViewModel.insertFavoriteMeal(mealEntity)

                showSnackBar(
                    view = requireView(),
                    message = "Item saved successfully",
                    duration = Snackbar.LENGTH_LONG,
                    actionText = getString(R.string.saved_snackBar),
                    action = { findNavController().navigate(R.id.action_recipeDetailFragment_to_favouriteFragment) }
                )
            } ?: run {
                CustomToast(requireContext(), "Meal detail data is missing", R.drawable.error_24px)
            }
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
