package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xapp.quickbit.data.source.remote.model.RecycleView
import com.xapp.quickbit.viewModel.adapter.SearchAdapter
import com.xapp.quickbit.databinding.FragmentSearchBinding
import java.util.ArrayList


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var newRecyclerView1: RecyclerView
    private lateinit var newRecyclerView2: RecyclerView
    private lateinit var newArrayList: ArrayList<RecycleView>
    private lateinit var searchView: SearchView
    private lateinit var imageId: Array<Int>
    private lateinit var heading: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        searchView = binding.search
        searchView.clearFocus()


        newRecyclerView1 = binding.recyclerview
        newRecyclerView1.layoutManager = LinearLayoutManager(context)
        newRecyclerView1.setHasFixedSize(true)

        newArrayList = arrayListOf()
        getUserData()
    }

    private fun getUserData() {
        for (i in imageId.indices) {
            val new = RecycleView(imageId[i], heading[i])
            newArrayList.add(new)

        }

        val adapter = SearchAdapter(newArrayList)
        newRecyclerView1.adapter = adapter

        newRecyclerView2 = binding.recyclerview2
        newRecyclerView2.layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


