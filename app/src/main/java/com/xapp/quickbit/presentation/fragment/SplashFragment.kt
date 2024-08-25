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
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentSplashBinding
import com.xapp.quickbit.presentation.activity.RecipeActivity

class SplashFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var _binding: FragmentSplashBinding
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            requireContext().getSharedPreferences("user_Info", AppCompatActivity.MODE_PRIVATE)
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
        val userEmail = sharedPreferences.getString("userEmail", null)
        val userPassword = sharedPreferences.getString("userPassword", null)
        return if (!userEmail.isNullOrEmpty() && !userPassword.isNullOrEmpty()) {
            val intent = Intent(requireContext(), RecipeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}
