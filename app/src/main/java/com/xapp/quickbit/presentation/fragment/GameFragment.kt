package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentGameBinding
import com.xapp.quickbit.viewModel.GameViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val gameViewModel: GameViewModel by viewModels()

    private var selectedMeal: MealDetail? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleOnClick()
    }

    private fun handleOnClick() {
        binding.btnGenerateRecipe.setOnClickListener {
            binding.starterLottie.visibility = View.GONE
            binding.randomCountLottie.visibility = View.VISIBLE
            gameViewModel.getRandomMeal()
            handleGameObserving()
        }

        binding.recipeCard.setOnClickListener {
            selectedMeal?.let {
                Log.d(ERROR_TAG, "Navigating to details with meal: $it")
                goToDetails(it)
            } ?: Log.e(ERROR_TAG, "Selected meal is null, cannot navigate")
        }
    }


    private fun handleGameObserving() {
        gameViewModel.randomMeal.observe(viewLifecycleOwner) { mealDetails ->
            mealDetails?.let {
                if (it.isNotEmpty()) {
                    Log.d(ERROR_TAG, "Meal received: $it")
                    showData(it[0])
                    selectedMeal = it[0]
                } else {
                    Log.e(ERROR_TAG, "No meals received")
                }
            } ?: Log.e(ERROR_TAG, "Meal details list is null")
            binding.randomCountLottie.visibility = View.GONE
            binding.starterLottie.visibility = View.GONE
        }

        gameViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                CustomNotifications.CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e(ERROR_TAG, it)
            }
        }
    }

    private fun showData(meal: MealDetail) {
        Glide.with(this@GameFragment)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.images_placeholder)
            .error(R.drawable.error_24px)
            .into(binding.ivRecycleViewImage)
        binding.ivRecycleViewText.text = meal.strMeal
        binding.tvCategoryIconText.text = meal.strCategory
        binding.tvLocationIconText.text = meal.strArea
    }

    private fun goToDetails(meal: MealDetail) {
        val bundle = Bundle().apply {
            putParcelable(GAME_PARCELABLE_KEY, meal)
        }
        findNavController().navigate(R.id.action_gameFragment_to_recipeDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ERROR_TAG = "GameFragment"
        const val GAME_PARCELABLE_KEY = "gameRecipeData"
    }
}
