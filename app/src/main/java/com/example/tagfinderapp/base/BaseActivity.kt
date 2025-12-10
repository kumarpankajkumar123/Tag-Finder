package com.example.tagfinderapp.base

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.tagfinderapp.R

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(window)
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.isAppearanceLightNavigationBars = true
        windowInsetsController?.isAppearanceLightStatusBars = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                val navBarInsets = insets.getInsets(WindowInsets.Type.navigationBars())
               /* view.setBackgroundColor(getColor(R.color.purple_700))         // Adjust padding to avoid overlap*/
                WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true

                view.setPadding(0, statusBarInsets.top, 0, navBarInsets.bottom)
                insets
            }
        }
        else{
            WindowCompat.setDecorFitsSystemWindows(window, false) // Let content draw under system bars
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
                val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = false
                view.setPadding(0, systemInsets.top, 0, systemInsets.bottom)
                insets
            }
            window.statusBarColor = ContextCompat.getColor(this, R.color.purple_700)
        }
    }
}