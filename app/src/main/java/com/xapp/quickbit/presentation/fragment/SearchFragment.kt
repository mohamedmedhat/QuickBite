package com.xapp.quickbit.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.MealDetail
import com.xapp.quickbit.databinding.FragmentSearchBinding
import com.xapp.quickbit.viewModel.SearchViewModel
import com.xapp.quickbit.viewModel.adapter.SearchAdapter
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import androidx.appcompat.widget.SearchView


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        searchAdapter = SearchAdapter(ArrayList()) { searchMealDetails ->
            navigateToItemFragment(searchMealDetails)
        }
        binding.rvSearchRecycleView.adapter = searchAdapter

        searchViewModel.searchRecipes.observe(viewLifecycleOwner) { searchRecipes ->
            searchAdapter.updateData(searchRecipes)
        }

        searchViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                CustomToast(requireContext(), it, R.drawable.error_24px)
                Log.e("Search Fragment Error", it)
            }
        }

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

    private fun navigateToItemFragment(mealDetail: MealDetail) {
        val bundle = Bundle().apply {
            putParcelable("searchMealDetail", mealDetail)
        }
        findNavController().navigate(R.id.action_searchFragment_to_recipeDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



