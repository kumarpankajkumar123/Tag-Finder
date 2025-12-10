package com.example.tagfinderapp.Util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import com.example.tagfinderapp.R

object ProgressDialog {

    private var dialog: Dialog? = null

    fun show(context: Context) {
        if (dialog?.isShowing == true) return

        dialog = Dialog(context)
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.progress_dialog) // your loader layout
            setCancelable(false)

            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            show()
        }
    }

    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }

    fun setStatusBarColor(window: Window, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.systemBars())
                view.setBackgroundColor(color)

                // Adjust padding to avoid overlap
                view.setPadding(0, statusBarInsets.top, 0, statusBarInsets.bottom)
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = color
            window.navigationBarColor = color
        }
    }
}
