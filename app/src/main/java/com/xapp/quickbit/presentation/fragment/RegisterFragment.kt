package com.xapp.quickbit.presentation.fragment

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
            requireContext().getSharedPreferences(
                USER_SHARED_PREFERENCE_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
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

        handleRegisterObserving()
        handleOnClicks()
        handleSwipeRefresh()
    }

    private fun handleRegisterObserving() {
        authViewModel.registrationResult.observe(viewLifecycleOwner) { errors ->
            binding.registerSwipeRefresh.isRefreshing = false
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
                binding.registerSwipeRefresh.isRefreshing = false
                authViewModel.saveUserToPreferences(authViewModel.user.value, requireContext())
                CustomToast(
                    requireContext(),
                    ContextCompat.getString(requireContext(), R.string.register_success_msg),
                    R.drawable.task_alt_24px
                )
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                CustomToast(
                    requireContext(),
                    ContextCompat.getString(requireContext(), R.string.register_failed_msg),
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
        editor.putString(USERNAME_SHARED_PREFERENCE_KEY, userName)
        editor.putString(EMAIL_PREFERENCE_KEY, email)
        editor.apply()
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.registerSwipeRefresh.setOnRefreshListener {
            refreshScreen()
        }
    }

    private fun styleSwipeRefresh() {
        binding.registerSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshScreen() {
        binding.tvlUserName.error = null
        binding.tvlEmail.error = null
        binding.tvlPassword.error = null
        binding.tvlConfirmPassword.error = null

        binding.tvUserName.text?.clear()
        binding.tvEmail.text?.clear()
        binding.tvPassword.text?.clear()
        binding.tvConfirmPassword.text?.clear()

        binding.registerSwipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_SHARED_PREFERENCE_NAME = "user_Info"
        const val USERNAME_SHARED_PREFERENCE_KEY = "userName"
        const val EMAIL_PREFERENCE_KEY = "email"
    }
}
