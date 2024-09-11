package com.xapp.quickbit.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.xapp.quickbit.R
import java.util.Locale


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val themePreference = findPreference<ListPreference>("theme_key")
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            changeTheme(newValue.toString())
            true
        }

        val languagePreference = findPreference<ListPreference>("language_key")
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            changeLanguage(newValue.toString())
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
        when (theme) {
            "green" -> {
                requireActivity().setTheme(R.style.Theme_Green)
            }

            "red" -> {
                requireActivity().setTheme(R.style.Theme_Red)
            }
        }
        requireActivity().recreate()
    }

    private fun changeLanguage(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = requireContext().resources.configuration
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)

        resources.updateConfiguration(config, resources.displayMetrics)

        requireActivity().recreate()
    }

    private fun displayDarkMode() {
        val darkModePreference = findPreference<SwitchPreferenceCompat>("dark_mode_key")
        if (darkModePreference != null) {
            val isDarkModeEnabled = darkModePreference.isChecked
            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            requireActivity().recreate()
        }
    }

    private fun goToGuide() {
        findNavController().navigate(R.id.action_settingsFragment_to_guideFragment)
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
