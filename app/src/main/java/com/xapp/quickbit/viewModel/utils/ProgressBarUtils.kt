package com.xapp.quickbit.viewModel.utils

import android.view.View
import android.widget.Button
import android.widget.ProgressBar

object ProgressBarUtils {

    fun showProgressBar(progressBar: ProgressBar, button: Button) {
        progressBar.visibility = View.VISIBLE
        button.text = ""
        button.isEnabled = false
    }

    fun hideProgressBar(progressBar: ProgressBar, button: Button, buttonText: String? = null) {
        progressBar.visibility = View.GONE
        button.text = buttonText
        button.isEnabled = true
    }
}