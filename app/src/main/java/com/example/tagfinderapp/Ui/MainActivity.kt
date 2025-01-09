package com.example.tagfinderapp.Ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tagfinderapp.Fragments.History
import com.example.tagfinderapp.Fragments.KeywordS
import com.example.tagfinderapp.Fragments.More
import com.example.tagfinderapp.Fragments.TagsFragmant
import com.example.tagfinderapp.Fragments.VideoDetail
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.DialogUtils
import com.example.tagfinderapp.Util.NetworkMonitor
import com.example.tagfinderapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast
    lateinit var binding: ActivityMainBinding
    lateinit var shared: SharedPreferences
    private var alertDialog: AlertDialog? = null
    private lateinit var networkMonitor: NetworkMonitor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkMonitor = NetworkMonitor(this) {
            reloadData() // Reload data when internet is restored
        }
        networkMonitor.startMonitoring()
        Log.e("internet monitor",""+networkMonitor)

            if (savedInstanceState == null) {
                // Dynamically add the Fragment
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.viewpager2,
                    VideoDetail()
                ) // Replace container view with Fragment
                fragmentTransaction.commit() // Commit the transaction
                Log.d("Automatically ", "default VideoDetail Fragment")
                binding.videoimage.drawable.setTint(ContextCompat.getColor(this, R.color.purple_700))
                binding.videotext.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
            }

            binding.video.setOnClickListener(this)
            binding.keyword.setOnClickListener(this)
            binding.history.setOnClickListener(this)
            binding.more.setOnClickListener(this)
        }

    override fun onClick(view: View?) {
        resetTabColors()
        when (view?.id) {
            binding.video.id -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.viewpager2,
                    VideoDetail()
                ) // Use replace to replace the fragment
                fragmentTransaction.commit()

                // Set the color of the selected tab to purple_700
                binding.videoimage.drawable.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_700
                    )
                )

                Log.d("FragmentNavigation", "Navigated to VideoDetail Fragment")

                binding.videotext.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
            }
            binding.keyword.id -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.viewpager2,
                    KeywordS()
                ) // Use replace to replace the fragment

                fragmentTransaction.commit()

                // Set the color of the selected tab to purple_700
                binding.keywordimage.drawable.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_700
                    )
                )
                Log.d("FragmentNavigation", "Navigated to KeywordS Fragment")

                binding.keywordtext.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
            }
            binding.history.id -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.viewpager2,
                    History()
                ) // Use replace to replace the fragment

                fragmentTransaction.commit()

                // Set the color of the selected tab to purple_700
                binding.historyimage.drawable.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_700
                    )
                )

                Log.d("FragmentNavigation", "Navigated to History Fragment")

                binding.historytext.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
            }
            binding.more.id -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.viewpager2,
                    More()
                ) // Use replace to replace the fragment

                fragmentTransaction.commit()

                Log.d("FragmentNavigation", "Navigated to More Fragment")

                // Set the color of the selected tab to purple_700
                binding.moreimage.drawable.setTint(ContextCompat.getColor(this, R.color.purple_700))
                binding.moretext.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
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
        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                super.onFragmentCreated(fm, f, savedInstanceState)
                Log.d("FragmentLifecycle", "Fragment Created: ${f.javaClass.simpleName}")
            }

            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
                Log.d("FragmentLifecycle", "Fragment Resumed: ${f.javaClass.simpleName}")
            }

            override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                super.onFragmentPaused(fm, f)
                Log.d("FragmentLifecycle", "Fragment Paused: ${f.javaClass.simpleName}")
            }
        }, true)
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onPause() {
        super.onPause()
        // Optionally handle any code for pausing activities like saving state or stopping resources
    }
    override fun onStop() {
        super.onStop()
        // Optionally handle any code when the activity is no longer visible
    }
    override fun onRestart() {
        super.onRestart()
        // Optionally handle any code when the activity is restarting
    }
    override fun onDestroy() {
        super.onDestroy()
        // Optionally handle any cleanup tasks like releasing resources
        networkMonitor.stopMonitoring()
    }

    private fun reloadData() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(
            R.id.viewpager2,
            VideoDetail()
        ) // Replace container view with Fragment
        fragmentTransaction.commit()
        Log.d("reload fragment mainActivity ", " reload videoDetail Fragment")
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.viewpager2)
        Log.d("onBackPressed", "Current Fragment: ${currentFragment?.javaClass?.simpleName}")

        when (currentFragment) {
            is TagsFragmant -> {
                // User is in TagsFragment, redirect to VideoDetail
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.viewpager2, VideoDetail())
                fragmentTransaction.commit()

                // Update the tab UI to reflect VideoDetail selection
                resetTabColors()
                binding.videoimage.drawable.setTint(ContextCompat.getColor(this, R.color.purple_700))
                binding.videotext.setTextColor(ContextCompat.getColor(this, R.color.purple_700))

                Log.d("onBackPressed", "Back button pressed in TagsFragment. Navigated to VideoDetail.")
            }
            is VideoDetail -> {
                // Default back behavior for VideoDetail (exit the app)
                super.onBackPressed()
            }
            else -> {
                // Default behavior for other fragments
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.viewpager2, VideoDetail())
                fragmentTransaction.commit()

                // Update the tab UI to reflect VideoDetail selection
                resetTabColors()
                binding.videoimage.drawable.setTint(ContextCompat.getColor(this, R.color.purple_700))
                binding.videotext.setTextColor(ContextCompat.getColor(this, R.color.purple_700))

                Log.d("onBackPressed", "Back button pressed. Navigated to VideoDetail.")
            }
        }
    }
}
