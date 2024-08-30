package com.xapp.quickbit.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.xapp.quickbit.R
import com.xapp.quickbit.databinding.ActivityRecipeBinding
import com.xapp.quickbit.databinding.DialogCustomSignOutBinding
import com.xapp.quickbit.viewModel.AuthViewModel

class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                R.id.myRecipesFragment, R.id.dashboardFragment, R.id.aboutFragment
            ),
            binding.drawerLayout
        )

        NavigationUI.setupWithNavController(binding.navigationView, navController)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    private fun welcomeUser() {
        val userName =
            getSharedPreferences("user_Info", MODE_PRIVATE).getString("userName", "Guest")
        binding.tvAppbarUsername.text = userName
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
