package com.example.tagfinderapp.Ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.tagfinderapp.ViewModal.TagsViewModel
import com.example.tagfinderapp.databinding.ActivitySplashScreenBinding


class SplashScreen : AppCompatActivity() {

    lateinit var binding : ActivitySplashScreenBinding
    private lateinit var tagsViewModel : TagsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageAnimator = ObjectAnimator.ofFloat(
            binding.videoimage,
            "translationY",
            -500f,
            500f
        ) // Adjust the values as per your screen size
        imageAnimator.setDuration(1000)

        val textAnimator =
            ObjectAnimator.ofFloat(binding.textview, "translationY", -500f, 500f) // Adjust similarly
        textAnimator.setDuration(1000)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(imageAnimator, textAnimator)
        animatorSet.start()

        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent: Intent = Intent(
                this,
                MainActivity::class.java
            )
            this@SplashScreen.startActivity(mainIntent)
            this@SplashScreen.finish()
        }, 2000)
    }
}