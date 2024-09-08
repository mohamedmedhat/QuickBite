package com.xapp.quickbit.presentation.fragment

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.UserEntity
import com.xapp.quickbit.databinding.DialogCustomSignOutBinding
import com.xapp.quickbit.databinding.FragmentProfileBinding
import com.xapp.quickbit.presentation.activity.AuthActivity
import com.xapp.quickbit.presentation.fragment.RegisterFragment.Companion.USER_SHARED_PREFERENCE_NAME
import com.xapp.quickbit.viewModel.AuthViewModel
import com.xapp.quickbit.viewModel.UserViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.showSnackBar
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.hideProgressBar
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.showProgressBar

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val userViewModel: UserViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private var profileImageUri: Uri? = null
    private var coverImageUri: Uri? = null

    private var isProfileImageRequest = false

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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (isProfileImageRequest) {
                getProfileImage.launch("image/*")
            } else {
                getCoverImage.launch("image/*")
            }
        } else {
            showSnackBar(
                requireView(),
                "Permission is required to access gallery.",
            )
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences(
                USER_SHARED_PREFERENCE_NAME,
                AppCompatActivity.MODE_PRIVATE
            )

        init()
        handleOnClicks()
    }


    private fun init() {
        val name = sharedPreferences.getString("userName", "Guest") ?: "Guest"
        val email = sharedPreferences.getString("email", null) ?: return
        saveUser(name, email)
        showUserData(email)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleOnClicks() {
        binding.btnEdit.setOnClickListener {
            binding.layoutEdit.visibility = View.VISIBLE
        }

        binding.ivEditProfileImage.setOnClickListener {
            checkPermissionAndPickImage(true)
        }

        binding.ivEditProfileCover.setOnClickListener {
            checkPermissionAndPickImage(false)
        }

        binding.btnSaveChanges.setOnClickListener {
            updateUserData()
            hideProgressBar(
                binding.btnSaveChangesProgressBar,
                binding.btnSaveChanges,
                ContextCompat.getString(requireContext(), R.string.save_changes)
            )
            binding.layoutEdit.visibility = View.GONE
        }

        binding.btnLogout.setOnClickListener {
            showSignOutConfirmationDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionAndPickImage(isProfileImageRequest: Boolean) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchContentPicker(isProfileImageRequest)
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                showPermissionRationale()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }

    private fun launchContentPicker(isProfileImageRequest: Boolean) {
        if (isProfileImageRequest) {
            getProfileImage.launch("image/*")
        } else {
            getCoverImage.launch("image/*")
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("The app needs access to your gallery to upload images.")
            .setPositiveButton("Grant Permission") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun saveUser(name: String, email: String) {
        val newUser = UserEntity(
            name = name,
            email = email
        )
        userViewModel.addUserData(newUser)
    }

    private fun updateUserData() {
        showProgressBar(binding.btnSaveChangesProgressBar, binding.btnSaveChanges)
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
                binding.profileSwipeRefresh.isRefreshing = false
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

    private fun showSignOutConfirmationDialog() {
        val dialogBinding = DialogCustomSignOutBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnConfirm.setOnClickListener {
            signOutUser()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun signOutUser() {
        authViewModel.signOut()
        with(
            requireContext().getSharedPreferences(
                USER_SHARED_PREFERENCE_NAME,
                AppCompatActivity.MODE_PRIVATE
            ).edit()
        ) {
            clear()
            apply()
        }
        startActivity(Intent(requireContext(), AuthActivity::class.java))
        requireActivity().finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
