package com.example.tagfinderapp.Util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.example.tagfinderapp.R


object DialogUtils {
    private var noInternetDialog: Dialog? = null

    fun showNoInternetDialog(context: Context) {
        if (noInternetDialog?.isShowing == true) return // Prevent multiple dialogs

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        val view = LayoutInflater.from(context).inflate(R.layout.internet_design, null)
        dialog.setContentView(view)

        val margin = (context.resources.displayMetrics.density * 32).toInt() // 32dp ~ adjustable
        dialog.window?.decorView?.setPadding(margin, 0, margin, 0)

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        noInternetDialog = dialog
        dialog.show()
    }

    fun dismissNoInternetDialog() {
        noInternetDialog?.dismiss()
        noInternetDialog = null
    }
}

