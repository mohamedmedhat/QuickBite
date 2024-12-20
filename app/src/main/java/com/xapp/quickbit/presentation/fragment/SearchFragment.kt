package com.xapp.quickbit.presentation.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentSearchBinding
import com.xapp.quickbit.viewModel.SearchViewModel
import com.xapp.quickbit.viewModel.adapter.SearchAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter
    private val searchViewModel: SearchViewModel by viewModels()

    private val voiceSearchLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val voiceData = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            voiceData?.let {
                binding.search.setQuery(it[0], true)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        setUpRecycleView()
        handleSearchObserving()
        handleSearchFunctionality()
        handleOnClick()
        handleSwipeRefresh()
    }

    private fun showLoading() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvSearchRecycleView.visibility = View.GONE
    }

    private fun showNoRecipesMessage(){
        binding.lottieLoading.visibility = View.GONE
        binding.rvSearchRecycleView.visibility = View.GONE
        binding.tvNoRecipes.visibility = View.VISIBLE
    }

    private fun setUpRecycleView() {
        searchAdapter = SearchAdapter(ArrayList()) { searchMealDetails ->
            navigateToItemFragment(searchMealDetails)
        }
        binding.rvSearchRecycleView.adapter = searchAdapter
    }

    private fun handleSearchObserving() {
        searchViewModel.searchRecipes.observe(viewLifecycleOwner) { searchRecipes ->
            binding.searchSwipeRefresh.isRefreshing = false
            if (!searchRecipes.isNullOrEmpty()) {
                binding.lottieLoading.visibility = View.GONE
                binding.tvNoRecipes.visibility = View.GONE
                binding.rvSearchRecycleView.visibility = View.VISIBLE
                searchAdapter.updateData(searchRecipes)
            } else {
                searchAdapter.updateData(emptyList())
                showNoRecipesMessage()
                binding.searchSwipeRefresh.isRefreshing = false
            }
        }

        searchViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.searchSwipeRefresh.isRefreshing = false
            showNoRecipesMessage()
            searchAdapter.updateData(emptyList())
            error?.let {
                CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e(ERROR_TAG, it)
            }
        }
    }

    private fun handleSearchFunctionality() {
        val searchView = binding.search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.fetchRecipesByName(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        if (arguments?.getBoolean("focus_search", false) == true) {
            searchView.requestFocus()
            searchView.isIconified = false
            searchView.setQuery("", false)
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun handleOnClick() {
        binding.btnVoiceSearch.setOnClickListener {
            handleVoiceSearch()
        }
    }

    private fun handleVoiceSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a recipe name")
        }

        try {
            voiceSearchLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            CustomToast(
                requireContext(),
                "Voice search not supported on your device",
                R.drawable.error_24px
            )
        }
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.searchSwipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun styleSwipeRefresh() {
        binding.searchSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshData() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.rvSearchRecycleView.visibility = View.GONE
        val currentQuery = binding.search.query.toString()
        if (currentQuery.isNotEmpty()) {
            searchViewModel.fetchRecipesByName(currentQuery)
        } else {
            binding.lottieLoading.visibility = View.GONE
            binding.rvSearchRecycleView.visibility = View.VISIBLE
            binding.searchSwipeRefresh.isRefreshing = false
        }
    }

    private fun navigateToItemFragment(mealDetail: MealDetail) {
        val bundle = Bundle().apply {
            putParcelable(SEARCH_MEAL_DETAIL_BUNDLE_KEY, mealDetail)
        }
        findNavController().navigate(R.id.action_searchFragment_to_recipeDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ERROR_TAG = "Search Fragment Error"
        const val SEARCH_MEAL_DETAIL_BUNDLE_KEY = "searchMealDetail"
    }
}
