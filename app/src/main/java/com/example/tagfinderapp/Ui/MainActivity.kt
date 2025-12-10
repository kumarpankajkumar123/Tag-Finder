package com.example.tagfinderapp.Ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.NetworkMonitor
import com.example.tagfinderapp.Util.ProgressDialog
import com.example.tagfinderapp.base.BaseActivity
import com.example.tagfinderapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    private lateinit var networkMonitor: NetworkMonitor
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        networkMonitor = NetworkMonitor(this) {
            runOnUiThread {
                if (navController.currentDestination?.id != R.id.videoFragment) {
                    navController.popBackStack(
                        navController.graph.startDestinationId,
                        false
                    )
                    navController.navigate(R.id.videoFragment)
                }
            }
        }

        networkMonitor.startMonitoring()
        Log.e("internet monitor", "" + networkMonitor)

        if (savedInstanceState == null) {
            selectVideoTab()

        }
        binding.video.setOnClickListener(this)
        binding.keyword.setOnClickListener(this)
        binding.history.setOnClickListener(this)
        binding.more.setOnClickListener(this)
        setupDestinationListener()

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false
        window.isNavigationBarContrastEnforced = false


        ProgressDialog.setStatusBarColor(window, ContextCompat.getColor(this, R.color.purple_700))

    }

    private fun setupDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            resetTabColors()
            when (destination.id) {
                R.id.videoFragment -> selectVideoTab()
                R.id.appTotorialFragment -> selectVideoTab()
                R.id.keywordFragment -> selectKeywordTab()
                R.id.subKeywordsFragment -> selectKeywordTab()
                R.id.historyFragment -> selectHistoryTab()
                R.id.historyFragment_to_tagsFragment -> selectHistoryTab()
                R.id.moreFragment -> selectMoreTab()
                R.id.tagsFragment -> {
                    selectVideoTab()
                }

            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.video.id -> {
                navigateBottom(R.id.videoFragment)
                Log.d("FragmentNavigation", "Navigated to VideoDetail Fragment")

            }

            binding.keyword.id -> {
                navigateBottom(R.id.keywordFragment)
                Log.d("FragmentNavigation", "Navigated to KeywordS Fragment")
            }

            binding.history.id -> {
                navigateBottom(R.id.historyFragment)
                Log.d("FragmentNavigation", "Navigated to History Fragment")
            }

            binding.more.id -> {
                navigateBottom(R.id.moreFragment)
                Log.d("FragmentNavigation", "Navigated to More Fragment")

            }
        }
    }

    private fun resetTabColors() {
        // Reset all tabs to black color
        binding.videoimage.drawable.setTint(ContextCompat.getColor(this, R.color.black))
        binding.videotext.setTextColor(ContextCompat.getColor(this, R.color.black))

        binding.keywordimage.drawable.setTint(ContextCompat.getColor(this, R.color.black))
        binding.keywordtext.setTextColor(ContextCompat.getColor(this, R.color.black))

        binding.historyimage.drawable.setTint(ContextCompat.getColor(this, R.color.black))
        binding.historytext.setTextColor(ContextCompat.getColor(this, R.color.black))

        binding.moreimage.drawable.setTint(ContextCompat.getColor(this, R.color.black))
        binding.moretext.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkMonitor.stopMonitoring()
    }

    private fun navigateBottom(destId: Int) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)                 // same destination pe duplicate na bane
            .setPopUpTo(R.id.videoFragment, false) // First se upar sab hata do, First ko mat hatao
            .build()

        // Agar already isi fragment pe ho toh navigate mat karo
        if (navController.currentDestination?.id == destId) return
        navController.navigate(destId, null, navOptions)
    }

    private fun selectVideoTab() {
        val selectedColor = ContextCompat.getColor(this, R.color.purple_700)
        binding.videoimage.drawable?.setTint(selectedColor)
        binding.videotext.setTextColor(selectedColor)
    }

    private fun selectKeywordTab() {
        val selectedColor = ContextCompat.getColor(this, R.color.purple_700)
        binding.keywordimage.drawable?.setTint(selectedColor)
        binding.keywordtext.setTextColor(selectedColor)
    }

    private fun selectHistoryTab() {
        val selectedColor = ContextCompat.getColor(this, R.color.purple_700)
        binding.historyimage.drawable?.setTint(selectedColor)
        binding.historytext.setTextColor(selectedColor)
    }

    private fun selectMoreTab() {
        val selectedColor = ContextCompat.getColor(this, R.color.purple_700)
        binding.moreimage.drawable?.setTint(selectedColor)
        binding.moretext.setTextColor(selectedColor)
    }
}
