package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity
import com.xapp.quickbit.data.source.remote.model.Meal
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentRecipeDetailBinding
import com.xapp.quickbit.viewModel.FavouriteRecipesViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import com.xapp.quickbit.viewModel.utils.CustomNotifications.showSnackBar

class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPermissionSharedPreferences: SharedPreferences
    private val favouriteRecipesViewModel: FavouriteRecipesViewModel by viewModels()

    private var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            requireContext().getSharedPreferences(
                "favourite_recipes_details",
                AppCompatActivity.MODE_PRIVATE
            )

        userPermissionSharedPreferences = requireContext().getSharedPreferences(
            "user_Info",
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

//    private fun init() {
//        val gson = Gson()
//        val mealJson = sharedPreferences.getString("favouriteMealJson", null)
//        val mealFromPrefs = gson.fromJson(mealJson, MealInformationEntity::class.java)
//
//        val mealDetail = arguments?.getParcelable<MealDetail>("mealDetail")
//            ?: arguments?.getParcelable("searchMealDetail")
//        val mealDetailDescription = arguments?.getParcelable<Meal>("categoryDetailsDetails")
//        val myCreatedRecipesDetails = arguments?.getParcelable<MyRecipesEntity>("myCreateRecipe")
//
//        Log.d("RecipeDetailFragment", "mealDetail: $mealDetail")
//        Log.d("RecipeDetailFragment", "mealFromPrefs: $mealFromPrefs")
//        Log.d("RecipeDetailFragment", "mealDetailDescription: $mealDetailDescription")
//        Log.d("RecipeDetailFragment", "myCreatedRecipesDetails: $myCreatedRecipesDetails")
//
//        // Chain let/apply blocks to handle each case
//        mealDetail?.let {
//            bindMealDetail(it)
//        } ?: mealFromPrefs?.let {
//            bindMealInformation(it)
//        } ?: mealDetailDescription?.let {
//            bindCategoryDetailData(it)
//        } ?: myCreatedRecipesDetails?.let {
//            bindMyCreatedRecipesDetails(it)
//        } ?: run {
//            CustomToast(requireContext(), "Meal detail data is missing", R.drawable.error_24px)
//            findNavController().popBackStack()
//        }
//    }

    private fun init() {
        val mealData = getMealData() ?: run {
            CustomToast(requireContext(), "Meal detail data is missing", R.drawable.error_24px)
            findNavController().popBackStack()
            return
        }
        bindData(mealData)
    }

    private fun getMealData(): Any? {
        val gson = Gson()
        val mealJson = sharedPreferences.getString("favouriteMealJson", null)
        val mealFromPrefs = gson.fromJson(mealJson, MealInformationEntity::class.java)

        val mealDetail = arguments?.getParcelable<MealDetail>("mealDetail")
            ?: arguments?.getParcelable("searchMealDetail")
        val mealDetailDescription = arguments?.getParcelable<Meal>("categoryDetailsDetails")
        val myCreatedRecipesDetails = arguments?.getParcelable<MyRecipesEntity>("myCreateRecipe")
            ?: arguments?.getParcelable<MyRecipesEntity>("dashboardRecipe")

        return mealDetail ?: mealFromPrefs ?: mealDetailDescription ?: myCreatedRecipesDetails
    }

    private fun bindData(mealData: Any) {
        when (mealData) {
            is MealDetail -> bindMealDetail(mealData)
            is MealInformationEntity -> bindMealInformation(mealData)
            is Meal -> bindCategoryDetailData(mealData)
            is MyRecipesEntity -> bindMyCreatedRecipesDetails(mealData)
        }
    }


    private fun bindMealDetail(meal: MealDetail) {
        binding.apply {
            Glide.with(this@RecipeDetailFragment)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.images_placeholder)
                .error(R.drawable.error_24px)
                .into(ivRecipeDetailImage)
            tvRecipeDetailTitle.text = meal.strMeal
            tvRecipeDetailsDescContent.text = meal.strInstructions
            tvCategoryIconText.text = meal.strCategory
            tvLocationIconText.text = meal.strArea
        }
    }

    private fun bindMealInformation(meal: MealInformationEntity) {
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
    }

    private fun bindCategoryDetailData(meal: Meal) {
        binding.apply {
            Glide.with(this@RecipeDetailFragment)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.images_placeholder)
                .error(R.drawable.error_24px)
                .into(ivRecipeDetailImage)
            tvRecipeDetailTitle.text = meal.strMeal
        }
    }

    private fun bindMyCreatedRecipesDetails(recipe: MyRecipesEntity) {
        binding.apply {
            Glide.with(this@RecipeDetailFragment)
                .load(recipe.mealThumb)
                .placeholder(R.drawable.images_placeholder)
                .error(R.drawable.error_24px)
                .into(ivRecipeDetailImage)
            tvRecipeDetailTitle.text = recipe.mealName
            tvCategoryIconText.text = recipe.mealCategory
            tvLocationIconText.text = recipe.mealCountry
            tvRecipeDetailsDescContent.text = recipe.mealInstruction
        }
    }

    private fun handleOnClick() {
        binding.fabBackToHome.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.fabSaveItem.setOnClickListener {
            checkIfItemSaveOrNot()
        }

        binding.ivYoutubeRecipe.setOnClickListener {
            val youtubeUrl = arguments?.getParcelable<MealDetail>("mealDetail")?.strYoutube
                ?: arguments?.getParcelable<MyRecipesEntity>("myCreateRecipe")?.mealYoutubeLink
            youtubeUrl?.let { url ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } catch (e: Exception) {
                    CustomToast(
                        requireContext(),
                        "No app available to open this link",
                        R.drawable.error_24px
                    )
                }
            } ?: CustomToast(requireContext(), "No YouTube link available", R.drawable.error_24px)
        }
    }

    private fun checkIfItemSaveOrNot() {
        // Get meal details and convert it to MealInformationEntity
        val mealDetail = arguments?.getParcelable<MealDetail>("mealDetail")
        mealDetail?.let {
            val mealEntity = MealInformationEntity(
                mealId = it.idMeal,
                mealName = it.strMeal,
                mealInstruction = it.strInstructions,
                mealThumb = it.strMealThumb,
                mealCategory = it.strCategory,
                mealCountry = it.strArea,
                mealYoutubeLink = it.strYoutube
            )

            isSaved = if (isSaved) {
                showConfirmationDeleteDialog(mealEntity)
                false
            } else {
                showConfirmationSaveDialog()
                true
            }
        }
    }

    private fun saveItemToFavourite() {
        val userType = userPermissionSharedPreferences.getString("userName", "Guest")
        val mealDetail = arguments?.getParcelable<MealDetail>("mealDetail")

        if (userType != "Guest") {
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
                CustomToast(
                    requireContext(),
                    "Meal detail data is missing",
                    R.drawable.error_24px
                )
            }
        } else {
            CustomToast(
                requireContext(),
                "You should have an account first to save a Recipe",
                R.drawable.error_24px
            )
        }
    }

    private fun deleteMealFromFavourite(meal: MealInformationEntity) {
        favouriteRecipesViewModel.deleteMeal(meal)
        showSnackBar(
            view = requireView(),
            message = "Item removed successfully",
            duration = Snackbar.LENGTH_LONG,
            action = { findNavController().navigate(R.id.action_recipeDetailFragment_to_favouriteFragment) }
        )
    }

    private fun showConfirmationDeleteDialog(meal: MealInformationEntity) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_save_recipe, null)
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)

        val alertDialog = dialogBuilder.create()

        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btndelete = dialogView.findViewById<Button>(R.id.btn_confirm)
        val title = dialogView.findViewById<TextView>(R.id.dialog_title)
        val message = dialogView.findViewById<TextView>(R.id.dialog_message)

        btndelete.text = "Delete"
        title.text = "Delete Item"
        message.text = "Are you sure you want to delete this item?"
        val redTint = ContextCompat.getColorStateList(requireContext(), R.color.red)
        btndelete.backgroundTintList = redTint
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btndelete.setOnClickListener {
            deleteMealFromFavourite(meal)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showConfirmationSaveDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_save_recipe, null)
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)

        val alertDialog = dialogBuilder.create()

        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnSave.setOnClickListener {
            saveItemToFavourite()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
