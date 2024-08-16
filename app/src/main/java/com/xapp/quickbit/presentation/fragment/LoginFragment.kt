package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentLoginBinding
import com.xapp.quickbit.presentation.activity.RecipeActivity
import com.xapp.quickbit.viewModel.AuthViewModel

class LoginFragment : Fragment() {
    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding

    private val viewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            handleOnClicks()
        } catch (error: IllegalArgumentException) {
            throw error
        }
    }


    private fun handleOnClicks() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(requireContext(), RecipeActivity::class.java)
            startActivity(intent)
        }

        binding.tvRegisterLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

}