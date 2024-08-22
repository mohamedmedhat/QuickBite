package com.xapp.quickbit.presentation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MealDB
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.ActivityMealDetailsBinding
import com.xapp.quickbit.presentation.fragment.HomeFragment.Companion.MEAL_ID
import com.xapp.quickbit.presentation.fragment.HomeFragment.Companion.MEAL_STR
import com.xapp.quickbit.presentation.fragment.HomeFragment.Companion.MEAL_THUMB
import com.xapp.quickbit.viewModel.DetailsViewModel

class MealDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealDetailsBinding
    private lateinit var detailsViewModel: DetailsViewModel
    private var mealId = ""
    private var mealStr = ""
    private var mealThumb = ""
    private var ytUrl = ""
    private lateinit var mealDetail: MealDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading()

        getMealInfoFromIntent()
        setUpViewWithMealInformation()
        setFloatingButtonState()

        detailsViewModel.getMealById(mealId)
        observeMealDetails()

        binding.imgYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ytUrl)))
        }

        binding.btnSave.setOnClickListener {
            handleSaveButtonClick()
        }
    }

    private fun observeMealDetails() {
        detailsViewModel.observeMealDetail().observe(this) { mealDetails ->
            mealDetails?.let {
                setTextsInViews(it[0])
                stopLoading()
            }
        }
    }

    private fun handleSaveButtonClick() {
        if (isMealSavedInDatabase()) {
            deleteMeal()
            binding.btnSave.setImageResource(R.drawable.ic_baseline_save_24)
            showSnackbar("Meal was deleted")
        } else {
            saveMeal()
            binding.btnSave.setImageResource(R.drawable.ic_saved)
            showSnackbar("Meal saved")
        }
    }

    private fun deleteMeal() {
        detailsViewModel.deleteMealById(mealId)
    }

    private fun setFloatingButtonState() {
        if (isMealSavedInDatabase()) {
            binding.btnSave.setImageResource(R.drawable.ic_saved)
        } else {
            binding.btnSave.setImageResource(R.drawable.ic_baseline_save_24)
        }
    }

    private fun isMealSavedInDatabase(): Boolean {
        return detailsViewModel.isMealSavedInDatabase(mealId)
    }

    private fun saveMeal() {
        val meal = MealDB(
            mealDetail.idMeal.toInt(),
            mealDetail.strMeal,
            mealDetail.strArea,
            mealDetail.strCategory,
            mealDetail.strInstructions,
            mealDetail.strMealThumb,
            mealDetail.strYoutube
        )
        detailsViewModel.insertMeal(meal)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.visibility = View.GONE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnSave.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }

    private fun setTextsInViews(meal: MealDetail) {
        this.mealDetail = meal
        ytUrl = meal.strYoutube
        binding.apply {
            tvInstructions.text = "- Instructions : "
            tvContent.text = meal.strInstructions
            tvAreaInfo.visibility = View.VISIBLE
            tvCategoryInfo.visibility = View.VISIBLE

            imgYoutube.visibility = View.VISIBLE
        }
    }

    private fun setUpViewWithMealInformation() {
        binding.apply {
            collapsingToolbar.title = mealStr
            Glide.with(applicationContext)
                .load(mealThumb)
                .into(imgMealDetail)
        }
    }

    private fun getMealInfoFromIntent() {
        intent?.let {
            mealId = it.getStringExtra(MEAL_ID) ?: ""
            mealStr = it.getStringExtra(MEAL_STR) ?: ""
            mealThumb = it.getStringExtra(MEAL_THUMB) ?: ""
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
}
