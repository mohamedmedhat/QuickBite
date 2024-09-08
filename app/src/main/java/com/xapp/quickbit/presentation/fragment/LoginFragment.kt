package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentLoginBinding
import com.xapp.quickbit.presentation.activity.RecipeActivity
import com.xapp.quickbit.presentation.fragment.RegisterFragment.Companion.USERNAME_SHARED_PREFERENCE_KEY
import com.xapp.quickbit.presentation.fragment.RegisterFragment.Companion.USER_SHARED_PREFERENCE_NAME
import com.xapp.quickbit.viewModel.AuthViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.hideProgressBar
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.showProgressBar


class LoginFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
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
                    ContextCompat.getString(requireContext(), R.string.google_signin_failed_msg),
                    R.drawable.error_24px
                )
                e.message?.let { Log.e(LOGIN_ERROR_TAG, it) }
            }
        }

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureGoogleSignIn()
        handleLogInObserving()
        handleOnClicks()
        handleSwipeRefresh()
    }

    private fun configureGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    private fun handleLogInObserving() {
        authViewModel.loginState.observe(viewLifecycleOwner) { isSuccess ->
            binding.loginSwipeRefresh.isRefreshing = false
            if (isSuccess) {
                authViewModel.saveUserToPreferences(authViewModel.user.value, requireContext())
                hideProgressBar(
                    binding.loginProgressBar,
                    binding.btnLogin,
                    ContextCompat.getString(requireContext(), R.string.login_btn_text)
                )
                CustomToast(
                    requireContext(),
                    ContextCompat.getString(requireContext(), R.string.login_success_msg),
                    R.drawable.task_alt_24px
                )
                goToIntroFragment()
            } else {
                CustomToast(
                    requireContext(),
                    ContextCompat.getString(requireContext(), R.string.auth_failed_msg),
                    R.drawable.error_24px
                )
            }
        }

        authViewModel.loginError.observe(viewLifecycleOwner) { errorMessage ->
            binding.loginSwipeRefresh.isRefreshing = false
            val emailError = errorMessage["email"]
            val passwordError = errorMessage["password"]

            binding.tvlEmail.error = emailError
            binding.tvlPassword.error = passwordError
            if (authViewModel.checkIfLoggedIn(requireContext())) {
                goToHomeActivity()
            }
        }
    }

    private fun handleOnClicks() {
        val editor = sharedPreferences.edit()
        binding.btnLogin.setOnClickListener {
            showProgressBar(
                binding.loginProgressBar,
                binding.btnLogin,
            )
            binding.tvlEmail.error = null
            binding.tvlPassword.error = null
            val email = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            authViewModel.handleLoginAction(email, password)
            editor.putString("userEmail", email)
            editor.putString("userPassword", password)
            editor.apply()
        }

        binding.tvRegisterLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            showProgressBar(binding.loginWithGoogleProgressBar, binding.btnLoginWithGoogle)
            signInWithGoogle()
            hideProgressBar(binding.loginWithGoogleProgressBar, binding.btnLoginWithGoogle)
        }

        binding.btnLoginAsGuest.setOnClickListener {
            showProgressBar(binding.loginAsGuestProgressBar, binding.btnLoginAsGuest)
            val editor = sharedPreferences.edit()
            editor.putString(USERNAME_SHARED_PREFERENCE_KEY, "Guest")
            editor.apply()
            navToRecipeActivity()
            hideProgressBar(
                binding.loginAsGuestProgressBar,
                binding.btnLoginAsGuest,
                ContextCompat.getString(requireContext(), R.string.login_as_guest_text)
            )
        }
    }

    private fun navToRecipeActivity() {
        val intent = Intent(requireContext(), RecipeActivity::class.java)
        startActivity(intent)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun goToIntroFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_introFragment)
    }

    private fun goToHomeActivity() {
        val intent = Intent(requireContext(), RecipeActivity::class.java)
        startActivity(intent)
    }

    private fun handleSwipeRefresh() {
        styleSwipeRefresh()
        binding.loginSwipeRefresh.setOnRefreshListener {
            refreshScreen()
        }
    }

    private fun styleSwipeRefresh() {
        binding.loginSwipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.darkGreen),
            ContextCompat.getColor(requireContext(), R.color.lightGreen),
            ContextCompat.getColor(requireContext(), R.color.lighterGreen),
        )
    }

    private fun refreshScreen() {
        binding.tvlEmail.error = null
        binding.tvlPassword.error = null
        binding.tvEmail.text?.clear()
        binding.tvPassword.text?.clear()
        binding.loginSwipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOGIN_ERROR_TAG = "LogIn"
    }
}
