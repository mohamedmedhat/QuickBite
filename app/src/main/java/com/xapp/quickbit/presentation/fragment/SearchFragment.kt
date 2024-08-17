package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.remote.model.myAdapter
import com.xapp.quickbit.data.source.remote.model.recycleView
import com.xapp.quickbit.databinding.FragmentSearchBinding
import com.xapp.quickbit.presentation.activity.RecipeActivity
import java.util.ArrayList


class SearchFragment : Fragment() {
    private var _binding:FragmentSearchBinding?=null
    private val binding get() = _binding!!
    private lateinit var newRecyclerView1: RecyclerView
    private lateinit var newRecyclerView2: RecyclerView
    private lateinit var newRecyclerView3: RecyclerView
    private lateinit var newArrayList: ArrayList<recycleView>
    private lateinit var searchView:SearchView
    lateinit var imageId:Array<Int>
    lateinit var heading:Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageId= arrayOf(
            R.drawable.ic_search ,
            R.drawable.ic_clear,
            R.drawable.searchbar,
            R.drawable.ic_search ,
            R.drawable.ic_clear,
            R.drawable.searchbar,
            R.drawable.ic_search ,
            R.drawable.ic_clear,
            R.drawable.searchbar,
            R.drawable.ic_search ,
            R.drawable.ic_clear,
            R.drawable.searchbar,
        )
heading= arrayOf(
    "hello",
    "hi",
    "nice",
    "hello",
    "hi",
    "nice",
    "hello",
    "hi",
    "nice",
    "hello",
    "hi",
    "nice",



)

        searchView=binding.search
        searchView.clearFocus()


        newRecyclerView1 =binding.recyclerview
        newRecyclerView1.layoutManager=LinearLayoutManager(context)
        newRecyclerView1.setHasFixedSize(true)

        newArrayList= arrayListOf<recycleView>()
        getUserData()
    }
    private fun getUserData() {
        for (i in imageId.indices) {
            val new = recycleView(imageId[i], heading[i])
            newArrayList.add(new)

        }

        val adapter = myAdapter(newArrayList)
        newRecyclerView1.adapter = adapter

        newRecyclerView2=binding.recyclerview2
        newRecyclerView2.layoutManager=LinearLayoutManager(context)
        adapter.setOnItemClickListener(object : myAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
        }

    })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
        // Inflate the layout for this fragment

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    }


