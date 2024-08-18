package com.xapp.quickbit.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    private lateinit var _binding: FragmentSplashBinding
    private val binding get() = _binding

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
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }, 4000)
    }
}