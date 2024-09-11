package com.xapp.quickbit.presentation.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import android.Manifest
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.ActivityRecipeBinding
import com.xapp.quickbit.presentation.fragment.RegisterFragment.Companion.USER_SHARED_PREFERENCE_NAME
import com.xapp.quickbit.viewModel.UserViewModel
import com.xapp.quickbit.viewModel.utils.CustomNotifications.CustomToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var speechRecognizer: SpeechRecognizer
    private val userViewModel: UserViewModel by viewModels()

    private val audioPermissionRequestCode = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = getSharedPreferences(USER_SHARED_PREFERENCE_NAME, MODE_PRIVATE)

        setupToolbar()
        setupNavigation()
        welcomeUser()
        handleOnClick()
        initSpeechRecognizer()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.fcv_recipeFragmentContainer)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.myRecipesFragment,
                R.id.dashboardFragment,
                R.id.profileFragment,
                R.id.gameFragment,
                R.id.settingsFragment
            ),
            binding.drawerLayout
        )

        NavigationUI.setupWithNavController(binding.navigationView, navController)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    private fun welcomeUser() {
        val userName = sharedPreference.getString("userName", "Guest")
        binding.tvAppbarUsername.text = userName
        val email = sharedPreference.getString("email", "null")

        if (email != null) {
            showUserData(email)
        }
    }

    private fun showUserData(email: String) {
        val headerView = binding.navigationView.getHeaderView(0)

        val drawerUserName = headerView.findViewById<TextView>(R.id.textViewName)
        val drawerUserEmail = headerView.findViewById<TextView>(R.id.textViewEmail)
        val drawerUserImage = headerView.findViewById<ImageView>(R.id.imageViewProfile)

        userViewModel.getUserData(email)
        userViewModel.userData.observe(this) { user ->
            user?.let {
                drawerUserName.text = it.name
                drawerUserEmail.text = it.email

                Glide.with(this)
                    .load(it.image)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.error_24px)
                    .into(binding.ivAppbarProfileImage)

                Glide.with(this)
                    .load(it.image)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.error_24px)
                    .into(drawerUserImage)
            }
        }
    }


    private fun handleOnClick() {
        binding.ivAppbarProfileImage.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationView)
        }

        binding.appbarMenu.setOnClickListener {
            binding.speechRecognitionLottie.visibility = View.VISIBLE
            binding.appbarMenu.visibility = View.GONE
            checkAudioPermission()
        }

        binding.speechRecognitionLottie.setOnClickListener {
            binding.speechRecognitionLottie.visibility = View.GONE
            binding.appbarMenu.visibility = View.VISIBLE
        }
    }

    private fun initSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}

                override fun onError(error: Int) {
                    val errorMessage = when (error) {
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout. Please try again."
                        SpeechRecognizer.ERROR_NO_MATCH -> "No match found. Please try again."
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error. Please check your microphone."
                        SpeechRecognizer.ERROR_NETWORK -> "Network error. Please check your connection."
                        else -> "Voice recognition failed. Please try again."
                    }
                    CustomToast(this@RecipeActivity, errorMessage, R.drawable.error_24px)
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    matches?.let {
                        val spokenText = it[0]
                        handleVoiceCommand(spokenText)
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        } else {
            CustomToast(
                this,
                "Speech recognition is not available on this device.",
                R.drawable.error_24px
            )
        }
    }

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                audioPermissionRequestCode
            )
        } else {
            startVoiceRecognition()
        }
    }


    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...")
        }
        try {
            speechRecognizer.startListening(intent)
        } catch (e: Exception) {
            CustomToast(
                this,
                ContextCompat.getString(
                    this@RecipeActivity,
                    R.string.voice_recognaizing_failed_msg
                ),
                R.drawable.error_24px
            )
        }
    }

    private fun handleVoiceCommand(command: String) {
        // Trim and convert the command to lowercase to make matching easier
        val cleanedCommand = command.trim().lowercase(Locale.ROOT)

        when {
            cleanedCommand.contains("open home") -> {
                findNavController(R.id.fcv_recipeFragmentContainer).navigate(R.id.action_homeFragment_to_recipeFragment)
            }

            cleanedCommand.contains("open saved") -> {
                findNavController(R.id.fcv_recipeFragmentContainer).navigate(R.id.action_homeFragment_to_favouriteFragment)
            }

            cleanedCommand.contains("open category") -> {
                findNavController(R.id.fcv_recipeFragmentContainer).navigate(R.id.action_homeFragment_to_categoryFragment)
            }

            cleanedCommand.contains("search for") -> {
                val query = cleanedCommand.removePrefix("search for").trim()
                searchForRecipe(query)
            }

            cleanedCommand.contains("open profile") -> {
                findNavController(R.id.fcv_recipeFragmentContainer).navigate(R.id.action_homeFragment_to_profileFragment)
            }

            cleanedCommand.contains("open dashboard") -> {
                findNavController(R.id.fcv_recipeFragmentContainer).navigate(R.id.action_homeFragment_to_dashboardFragment)
            }

            cleanedCommand.contains("open my recipes") -> {
                findNavController(R.id.fcv_recipeFragmentContainer).navigate(R.id.action_homeFragment_to_myRecipesFragment)
            }

            cleanedCommand.contains("open the game") -> {
                findNavController(R.id.fcv_recipeFragmentContainer).navigate(R.id.action_homeFragment_to_gameFragment)
            }

            else -> {
                CustomToast(
                    this,
                    getString(R.string.unknown_command),
                    R.drawable.error_24px
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            audioPermissionRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start voice recognition
                    startVoiceRecognition()
                } else {
                    // Permission denied, show a message
                    CustomToast(
                        this,
                        "Microphone permission is required to use voice recognition",
                        R.drawable.error_24px
                    )
                }
            }
        }
    }


    private fun searchForRecipe(query: String) {
        val bundle = Bundle()
        bundle.putString("query", query)
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_homeFragment_to_searchFragment,
            bundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}
