package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.FragmentIntroBinding
import com.xapp.quickbit.presentation.activity.RecipeActivity
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.hideProgressBar
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.showProgressBar

class IntroFragment : Fragment() {
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.introBtnGoToHome.setOnClickListener {
            showProgressBar(binding.introBtnProgressBar, binding.introBtnGoToHome)
            val intent = Intent(requireContext(), RecipeActivity::class.java)
            startActivity(intent)
            hideProgressBar(
                binding.introBtnProgressBar,
                binding.introBtnGoToHome,
                ContextCompat.getString(requireContext(), R.string.get_started)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}