package com.xapp.quickbit.presentation.fragment


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.xapp.quickbit.R
import com.xapp.quickbit.data.source.local.entity.MyRecipesEntity
import com.xapp.quickbit.databinding.FragmentCreateRecipeBinding
import com.xapp.quickbit.viewModel.MyRecipesViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.showSnackBar
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.hideProgressBar
import com.xapp.quickbit.viewModel.utils.ProgressBarUtils.showProgressBar

class CreateRecipeFragment : Fragment() {

    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyRecipesViewModel by viewModels()
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    private var isImageRequest: Boolean = false

    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                binding.ivRecipeImage.setImageURI(it)
            }
        }

    private val getVideoResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                videoUri = it
                binding.ivVideoThumbnail.visibility = View.VISIBLE
                // Use MediaMetadataRetriever to get a thumbnail for the video
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(requireContext(), it)
                val bitmap = retriever.getFrameAtTime(0)
                binding.ivVideoThumbnail.setImageBitmap(bitmap)
                retriever.release()
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                if (isImageRequest) {
                    getImageResult.launch("image/*")
                } else {
                    getVideoResult.launch("video/*")
                }
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.permission_denied_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleOnClick()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleOnClick() {
        binding.btnAddImage.setOnClickListener {
            isImageRequest = true // Set to true for image
            checkPermission(Manifest.permission.READ_MEDIA_IMAGES, true)
        }

        binding.btnUploadVideo.setOnClickListener {
            isImageRequest = false // Set to false for video
            checkPermission(Manifest.permission.READ_MEDIA_VIDEO, false)
        }

        binding.btnSubmit.setOnClickListener {
            showProgressBar(binding.btnSubmitProgressBar, binding.btnSubmit)
            saveRecipe()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission(permission: String, isImage: Boolean) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchContentPicker(isImage)
            }

            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionRationale(permission)
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun launchContentPicker(isImage: Boolean) {
        if (isImage) {
            getImageResult.launch("image/*")
        } else {
            getVideoResult.launch("video/*")
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationale(permission: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("The app needs access to your gallery to upload ${if (isImageRequest) "images" else "videos"}.")
            .setPositiveButton("Grant Permission") { _, _ ->
                requestPermissionLauncher.launch(permission)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }


    private fun saveRecipe() {
        val name = binding.etRecipeName.text.toString().trim()
        val description = binding.etRecipeDescription.text.toString().trim()
        val location = binding.spinnerLocation.selectedItem.toString()
        val category = binding.spinnerCategory.selectedItem.toString()
        val videoUrl = binding.etVideoUrl.text.toString().trim()
        val imageUriString = imageUri?.toString() ?: ""
        val videoUriString = videoUri?.toString() ?: ""

        if (name.isEmpty() || description.isEmpty()) {
            hideProgressBar(
                binding.btnSubmitProgressBar,
                binding.btnSubmit,
                ContextCompat.getString(requireContext(), R.string.add_recipe)
            )
            Snackbar.make(
                requireView(),
                ContextCompat.getString(requireContext(), R.string.name_and_desc_cant_be_empty),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (videoUriString.isNotEmpty() && videoUrl.isNotEmpty()) {
            hideProgressBar(
                binding.btnSubmitProgressBar,
                binding.btnSubmit,
                ContextCompat.getString(requireContext(), R.string.add_recipe)
            )
            Snackbar.make(
                requireView(),
                ContextCompat.getString(requireContext(), R.string.choose_video_or_url_msg),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (videoUriString.isEmpty() && videoUrl.isEmpty()) {
            hideProgressBar(
                binding.btnSubmitProgressBar,
                binding.btnSubmit,
                ContextCompat.getString(requireContext(), R.string.add_recipe)
            )
            Snackbar.make(
                requireView(),
                ContextCompat.getString(requireContext(), R.string.please_provide_video_url_msg),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val recipe = MyRecipesEntity(
            mealName = name,
            mealCountry = location,
            mealCategory = category,
            mealInstruction = description,
            mealThumb = imageUriString,
            mealYoutubeLink = videoUrl.ifEmpty { videoUriString },
        )

        viewModel.insertRecipe(recipe)
        hideProgressBar(
            binding.btnSubmitProgressBar,
            binding.btnSubmit,
            ContextCompat.getString(requireContext(), R.string.add_recipe)
        )
        showSnackBar(
            view = requireView(),
            message = ContextCompat.getString(
                requireContext(),
                R.string.item_created_successfully_msg
            ),
            duration = Snackbar.LENGTH_LONG,
            actionText = getString(R.string.saved_snackBar),
            action = { findNavController().navigate(R.id.action_createRecipeFragment_to_myRecipesFragment) }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
