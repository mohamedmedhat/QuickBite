package com.xapp.quickbit.presentation.fragment

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentRegisterBinding
import com.xapp.quickbit.viewModel.AuthViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast

class RegisterFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            requireContext().getSharedPreferences("user_Info", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleOnClicks()

        authViewModel.registrationResult.observe(viewLifecycleOwner) { errors ->
            val userNameError = errors["userName"]
            val emailError = errors["email"]
            val passwordError = errors["password"]
            val confirmPasswordError = errors["confirmPassword"]

            binding.tvlUserName.error = userNameError
            binding.tvlEmail.error = emailError
            binding.tvlPassword.error = passwordError
            binding.tvlConfirmPassword.error = confirmPasswordError
        }

        authViewModel.registerState.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                authViewModel.saveUserToPreferences(authViewModel.user.value, requireContext())
                CustomToast(
                    requireContext(),
                    "You have registered successfully",
                    R.drawable.task_alt_24px
                )
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                CustomToast(
                    requireContext(),
                    "Registration failed. Please try again.",
                    R.drawable.error_24px
                )
            }
        }
    }

    private fun handleOnClicks() {

        binding.btnSignUp.setOnClickListener {
            val userName = binding.tvUserName.text.toString()
            val email = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            val confirmPassword = binding.tvConfirmPassword.text.toString()

            authViewModel.handleRegisterAction(userName, email, password, confirmPassword)
            saveUserDataToSharedPreferences(userName, email)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun saveUserDataToSharedPreferences(userName: String, email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userName", userName)
        editor.putString("email", email)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
