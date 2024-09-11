package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.xapp.quickbit.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val themePreference = findPreference<ListPreference>("theme_key")
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            changeTheme(newValue.toString())
            true
        }

        val darkModePreference = findPreference<SwitchPreferenceCompat>("dark_mode_key")
        darkModePreference?.setOnPreferenceClickListener {
            displayDarkMode()
            true
        }

        val guidePreference = findPreference<Preference>("guide_key")
        guidePreference?.setOnPreferenceClickListener {
            goToGuide()
            true
        }

        val feedbackPreference = findPreference<Preference>("feedback_key")
        feedbackPreference?.setOnPreferenceClickListener {
            openFeedbackForm()
            true
        }

    }

    private fun changeTheme(theme: String) {
        // Apply the theme dynamically
        when (theme) {
            "green" -> { /* Set green theme */
            }

            "white" -> { /* Set white theme */
            }
            // Handle other themes
        }
    }

    private fun displayDarkMode() {

    }

    private fun goToGuide() {

    }

    private fun openFeedbackForm() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@quickbite.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for QuickBite")
        }
        startActivity(intent)
    }

}
