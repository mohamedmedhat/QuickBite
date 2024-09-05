package com.xapp.quickbit.presentation.fragment


import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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

class CreateRecipeFragment : Fragment() {

    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyRecipesViewModel by viewModels()
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleOnClick()
    }

    private fun handleOnClick() {
        binding.btnAddImage.setOnClickListener {
            getImageResult.launch("image/*")
        }

        binding.btnUploadVideo.setOnClickListener {
            getVideoResult.launch("video/*")
        }

        binding.btnSubmit.setOnClickListener {
            saveRecipe()
        }
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
            Snackbar.make(
                requireView(),
                ContextCompat.getString(requireContext(), R.string.name_and_desc_cant_be_empty),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (videoUriString.isNotEmpty() && videoUrl.isNotEmpty()) {
            Snackbar.make(
                requireView(),
                ContextCompat.getString(requireContext(), R.string.choose_video_or_url_msg),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        if (videoUriString.isEmpty() && videoUrl.isEmpty()) {
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
