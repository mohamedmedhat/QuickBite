package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentRegisterBinding
import com.xapp.quickbit.viewModel.AuthViewModel
import java.lang.IllegalArgumentException

class RegisterFragment : Fragment() {
    private lateinit var _binding: FragmentRegisterBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            val userName = binding.tvUserName.text.toString()
            val email = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            val confirmPassword = binding.tvConfirmPassword.text.toString()

            authViewModel.handleRegisterAction(userName, email, password, confirmPassword)
        }

        authViewModel.registrationResult.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (message == "You have signed up successfully") {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }
}