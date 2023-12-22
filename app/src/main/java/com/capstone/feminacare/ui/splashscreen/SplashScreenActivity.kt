package com.capstone.feminacare.ui.splashscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.feminacare.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}