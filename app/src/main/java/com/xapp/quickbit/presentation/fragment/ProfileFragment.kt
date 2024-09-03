package com.xapp.quickbit.presentation.fragment

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.UserEntity
import com.xapp.quickbit.databinding.FragmentProfileBinding
import com.xapp.quickbit.viewModel.UserViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val userViewModel: UserViewModel by viewModels()
    private var profileImageUri: Uri? = null
    private var coverImageUri: Uri? = null

    private val getProfileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                profileImageUri = it
                binding.ivEditProfileImage.setImageURI(it)
            }
        }

    private val getCoverImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                coverImageUri = it
                binding.ivEditProfileCover.setImageURI(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences("user_Info", AppCompatActivity.MODE_PRIVATE)

        init()
        handleOnClicks()
    }


    private fun init() {
        val name = sharedPreferences.getString("userName", "Guest") ?: "Guest"
        val email = sharedPreferences.getString("email", null) ?: return
        saveUser(name, email)
        showUserData(email)
    }

    private fun handleOnClicks() {
        binding.btnEdit.setOnClickListener {
            binding.layoutEdit.visibility = View.VISIBLE
        }

        binding.ivEditProfileCover.setOnClickListener {
            getCoverImage.launch("image/*")
        }

        binding.ivEditProfileImage.setOnClickListener {
            getProfileImage.launch("image/*")
        }

        binding.btnSaveChanges.setOnClickListener {
            updateUserData()
            binding.layoutEdit.visibility = View.GONE
        }
    }

    private fun saveUser(name: String, email: String) {
        val newUser = UserEntity(
            name = name,
            email = email
        )
        userViewModel.addUserData(newUser)
    }

    private fun updateUserData() {
        val editName = binding.etEditName.text.toString()
        val email = sharedPreferences.getString("email", null) ?: return
        val profileImageUriString = profileImageUri?.toString() ?: ""
        val coverImageUriString = coverImageUri?.toString() ?: ""

        val updatedUser = UserEntity(
            name = editName,
            email = email,
            image = profileImageUriString,
            cover = coverImageUriString
        )

        userViewModel.updateUserData(updatedUser)
    }

    private fun showUserData(email: String) {
        userViewModel.getUserData(email)
        userViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                Glide.with(this)
                    .load(it.image)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.error_24px)
                    .into(binding.ivProfileImage)

                Glide.with(this)
                    .load(it.cover)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.error_24px)
                    .into(binding.ivProfileCover)

                binding.tvProfileName.text = it.name
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
