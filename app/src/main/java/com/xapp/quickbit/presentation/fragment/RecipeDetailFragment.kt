package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MealInformationEntity
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity
import com.xapp.quickbit.data.source.remote.model.Meal
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentRecipeDetailBinding
import com.xapp.quickbit.presentation.fragment.CategoryDetailsFragment.Companion.CATEGORY_DETAILS_DETAILS_BUNDLE_KEY
import com.xapp.quickbit.presentation.fragment.DashboardFragment.Companion.DASHBOARD_BUNDLE_KEY
import com.xapp.quickbit.presentation.fragment.FavouriteFragment.Companion.KEY_FAVOURITE_MEAL_BUNDLE_KEY
import com.xapp.quickbit.presentation.fragment.GameFragment.Companion.GAME_PARCELABLE_KEY
import com.xapp.quickbit.presentation.fragment.HomeFragment.Companion.HOME_AREA_BUNDLE_KEY
import com.xapp.quickbit.presentation.fragment.HomeFragment.Companion.HOME_BUNDLE_KEY
import com.xapp.quickbit.presentation.fragment.MyRecipesFragment.Companion.CREATED_RECIPE_BUNDLE_KEY
import com.xapp.quickbit.presentation.fragment.SearchFragment.Companion.SEARCH_MEAL_DETAIL_BUNDLE_KEY
import com.xapp.quickbit.viewModel.FavouriteRecipesViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import com.xapp.quickbit.viewModel.utils.CustomNotifications.showSnackBar

class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPermissionSharedPreferences: SharedPreferences
    private lateinit var youtubePlayerView: YouTubePlayerView

    private val favouriteRecipesViewModel: FavouriteRecipesViewModel by viewModels()

    private var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        ensureYoutubePlayerObservingLifeCycle()
        init()
        handleOnClick()
        handleSwipeRefresh()
    }

    private fun ensureYoutubePlayerObservingLifeCycle() {
        youtubePlayerView = binding.recipeDetailYoutubePlayer
        lifecycle.addObserver(youtubePlayerView)
    }

    private fun init() {
        val mealData = getMealData() ?: run {
            CustomToast(requireContext(), "Meal detail data is missing", R.drawable.error_24px)
            findNavController().popBackStack()
            return
        }
        bindData(mealData)
    }

    private fun getMealDetailFromArguments(): MealDetail? {
        val keys = listOf(
            HOME_BUNDLE_KEY,
            SEARCH_MEAL_DETAIL_BUNDLE_KEY,
            GAME_PARCELABLE_KEY,
            HOME_AREA_BUNDLE_KEY,
            CATEGORY_DETAILS_DETAILS_BUNDLE_KEY
        )

        return keys.asSequence()
            .mapNotNull { key -> arguments?.getParcelable<MealDetail>(key) }
            .firstOrNull()
    }

    private fun getMealInformationEntityFromArguments(): MyRecipesEntity? {
        val keys = listOf(
            CREATED_RECIPE_BUNDLE_KEY,
            DASHBOARD_BUNDLE_KEY
        )
        return keys.asSequence()
            .mapNotNull { key -> arguments?.getParcelable<MyRecipesEntity>(key) }
            .firstOrNull()
    }

    private fun getMealData(): Any? {

        val mealFromPrefs =
            arguments?.getParcelable<MealInformationEntity>(KEY_FAVOURITE_MEAL_BUNDLE_KEY)

        val mealDetail = getMealDetailFromArguments()

        val myCreatedRecipesDetails = getMealInformationEntityFromArguments()

        return mealDetail ?: mealFromPrefs ?: myCreatedRecipesDetails
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

        binding.fabShareSocial.setOnClickListener {
            shareRecipe()
        }

        binding.fabSaveItem.setOnClickListener {
            checkIfItemSaveOrNot()
        }

        binding.ivYoutubeRecipe.setOnClickListener {
            showYouTubePlayer()
        }

    }

    private fun showYouTubePlayer() {
        val youtubeUrl = getMealDetailFromArguments()?.strYoutube
            ?: getMealInformationEntityFromArguments()?.mealYoutubeLink
            ?: arguments?.getParcelable<MealInformationEntity>(KEY_FAVOURITE_MEAL_BUNDLE_KEY)?.mealYoutubeLink

        youtubeUrl?.let { url ->
            val videoId = extractYoutubeVideoId(url)
            if (videoId != null) {
                youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
                binding.recipeDetailYoutubePlayer.visibility = View.VISIBLE
            } else {
                CustomToast(requireContext(), "Invalid YouTube link", R.drawable.error_24px)
            }
        } ?: CustomToast(requireContext(), "No YouTube link available", R.drawable.error_24px)
    }

    private fun extractYoutubeVideoId(youtubeUrl: String): String? {
        val regex =
            "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?v%3D|^youtu.be\\/|\\/embed\\/)([^#&?\\n]+)".toRegex()
        val match = regex.find(youtubeUrl)
        return match?.value
    }

    private fun checkIfItemSaveOrNot() {
        val mealDetail = getMealDetailFromArguments()
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
        val mealDetail = getMealDetailFromArguments()
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

    private fun shareRecipe() {
        val mealDetail = getMealDetailFromArguments()

        mealDetail?.let { meal ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"

                val shareText = """
                Check out this recipe:
                
                Recipe Name: ${meal.strMeal}
                Description: ${meal.strInstructions}
                Category: ${meal.strCategory}
                Area: ${meal.strArea}
                Watch Video: ${meal.strYoutube}
            """.trimIndent()

                putExtra(Intent.EXTRA_SUBJECT, "Check out this recipe: ${meal.strMeal}")
                putExtra(Intent.EXTRA_TEXT, shareText)

                meal.strMealThumb.let { thumbUrl ->
                    val imageUri = Uri.parse(thumbUrl)
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }
            startActivity(Intent.createChooser(shareIntent, "Share Recipe via"))
        } ?: run {
            CustomToast(
                requireContext(),
                "Meal detail data is missing",
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

        btndelete.text = ContextCompat.getString(requireContext(), R.string.delete_dialog_btn_title)
        title.text = ContextCompat.getString(requireContext(), R.string.delete_dialog_title)
        message.text = ContextCompat.getString(requireContext(), R.string.delete_dialog_message)
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

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.recipeDetailSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.recipeDetailSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        init()
        binding.recipeDetailSwipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView.release()
    }
}
