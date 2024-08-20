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
<<<<<<< HEAD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
=======
    private lateinit var _binding: FragmentSplashBinding
    private val binding get() = _binding
>>>>>>> 59c289dd795c237dd9bdc9fe70d59d2079ab864e

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

<<<<<<< HEAD

}
=======
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animate = AnimationUtils.loadAnimation(requireContext(), R.anim.splash_anim)
        binding.splashLogo.startAnimation(animate)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }, 800)
    }
}
>>>>>>> 59c289dd795c237dd9bdc9fe70d59d2079ab864e
