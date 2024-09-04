package com.xapp.quickbit.presentation.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.ActivityRecipeBinding
import com.xapp.quickbit.databinding.DialogCustomSignOutBinding
import com.xapp.quickbit.viewModel.AuthViewModel
import com.xapp.quickbit.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedPreference: SharedPreferences
    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = getSharedPreferences("user_Info", MODE_PRIVATE)

        setupToolbar()
        setupNavigation()
        welcomeUser()
        handleOnClick()
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
                R.id.myRecipesFragment, R.id.dashboardFragment, R.id.profileFragment
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
            showSignOutConfirmationDialog()
        }
    }

    private fun showSignOutConfirmationDialog() {
        val dialogBinding = DialogCustomSignOutBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
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
        with(getSharedPreferences("user_Info", MODE_PRIVATE).edit()) {
            clear()
            apply()
        }
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
