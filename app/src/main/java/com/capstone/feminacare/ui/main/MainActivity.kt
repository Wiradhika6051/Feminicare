package com.capstone.feminacare.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.ActivityMainBinding
import com.capstone.feminacare.ui.ViewModelFactory
import com.capstone.feminacare.ui.bloodcheckup.BloodCheckupResultActivity
import com.capstone.feminacare.utils.CAPTURED_IMAGE_URI
import com.capstone.feminacare.utils.ImageUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance()
    }
    private var captureImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.background = null
        navView.menu.getItem(2).isEnabled = false

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        binding.floatingActionButton.setOnClickListener {
            startCamera()
        }
    }

    private fun startCamera() {
        captureImage = ImageUtils.getImageUri(this)
        launcherIntentCamera.launch(captureImage)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val intent = Intent(this, BloodCheckupResultActivity::class.java)
            intent.putExtra(CAPTURED_IMAGE_URI, captureImage.toString())
            startActivity(intent)
        }
    }

}