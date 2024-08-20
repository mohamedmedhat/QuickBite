package com.xapp.quickbit.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentSearchBinding
import com.xapp.quickbit.presentation.activity.AuthActivity
import com.xapp.quickbit.presentation.activity.RecipeActivity


class AboutFragment : Fragment() {
    private var _binding: FragmentSearchBinding?=null
    private val binding get() = _binding!!
    private lateinit var btn1:Button
    private lateinit var btn2:Button
    private lateinit var window:TextView
    private lateinit var image:ImageView
    private val image_request_code:Int=100
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    btn1.findViewById<Button>(R.id.signOut)
        btn1.setOnClickListener{

        }
        btn2.findViewById<Button>(R.id.about)
        btn2.setOnClickListener {
            window.setTransitionVisibility(View.VISIBLE)
        }
        pickImageGallery()

    }

private fun pickImageGallery(){
    val intent=Intent(Intent.ACTION_PICK)
    intent.type="image/*"
    startActivityForResult(intent,image_request_code)
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == image_request_code && resultCode==RESULT_OK ){
            image.setImageURI(data?.data)
        }
    }
    }
