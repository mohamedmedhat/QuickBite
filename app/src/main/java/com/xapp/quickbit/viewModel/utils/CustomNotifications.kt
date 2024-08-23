package com.xapp.quickbit.viewModel.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.xapp.quickbit.R

object CustomNotifications {

    fun showSnackBar(
        view: View,
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        actionText: String? = null,
        action: ((View) -> Unit)? = null
    ) {
        val snackBar = Snackbar.make(view, message, duration)
        actionText?.let {
            snackBar.setAction(it, action)
        }
        snackBar.show()
    }


    @SuppressLint("InflateParams")
    fun CustomToast(context: Context, message: String, iconResId: Int? = null) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)

        toastText.text = message
        iconResId?.let {
            toastIcon.setImageDrawable(ContextCompat.getDrawable(context, it))
            toastIcon.visibility = View.VISIBLE
        } ?: run {
            toastIcon.visibility = View.GONE
        }

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}