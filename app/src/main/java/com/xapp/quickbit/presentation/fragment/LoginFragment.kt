package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentLoginBinding
import com.xapp.quickbit.presentation.activity.RecipeActivity
import com.xapp.quickbit.viewModel.AuthViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    authViewModel.firebaseAuthWithGoogle(idToken)
                }
            } catch (e: ApiException) {
                CustomToast(
                    requireContext(),
                    "Google sign-in failed: ${e.message}",
                    R.drawable.error_24px
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureGoogleSignIn()
        handleOnClicks()

        authViewModel.loginState.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                authViewModel.saveUserToPreferences(authViewModel.user.value, requireContext())
                CustomToast(
                    requireContext(),
                    "you have login successfully",
                    R.drawable.task_alt_24px
                )
                goToHomeActivity()
            } else {
                CustomToast(requireContext(), "Authentication failed.", R.drawable.error_24px)
            }
        }

        authViewModel.loginError.observe(viewLifecycleOwner) { errorMessage ->
            val emailError = errorMessage["email"]
            val passwordError = errorMessage["password"]

            binding.tvlEmail.error = emailError
            binding.tvlPassword.error = passwordError
            if (authViewModel.checkIfLoggedIn(requireContext())) {
                goToHomeActivity()
            }
        }
    }

    private fun configureGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    private fun handleOnClicks() {
        binding.btnLogin.setOnClickListener {
            binding.tvlEmail.error = null
            binding.tvlPassword.error = null
            val email = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            authViewModel.handleLoginAction(email, password)
        }

        binding.tvRegisterLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun goToHomeActivity() {
        val intent = Intent(activity, RecipeActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
