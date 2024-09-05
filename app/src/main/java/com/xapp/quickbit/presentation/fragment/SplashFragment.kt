package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentSplashBinding
import com.xapp.quickbit.presentation.activity.RecipeActivity
import com.xapp.quickbit.presentation.fragment.RegisterFragment.Companion.USER_SHARED_PREFERENCE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var _binding: FragmentSplashBinding
    private val binding get() = _binding


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
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animate = AnimationUtils.loadAnimation(requireContext(), R.anim.splash_anim)
        binding.splashLogo.startAnimation(animate)

        Handler(Looper.getMainLooper()).postDelayed({
            checkIfUserIsLoggedInBefore()
        }, 800)
    }

    private fun checkIfUserIsLoggedInBefore() {
        lifecycleScope.launch(Dispatchers.Default) {
            val userEmail = sharedPreferences.getString("userEmail", null)
            val userPassword = sharedPreferences.getString("userPassword", null)

            withContext(Dispatchers.Main) {
                if (!userEmail.isNullOrEmpty() && !userPassword.isNullOrEmpty()) {
                    navigateToRecipeActivity()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }

    }

    private fun navigateToRecipeActivity() {
        val intent = Intent(requireActivity(), RecipeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        requireActivity().finish()
    }

}
