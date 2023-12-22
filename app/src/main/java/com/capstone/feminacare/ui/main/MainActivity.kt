package com.capstone.feminacare.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.Interpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.ActivityMainBinding
import com.capstone.feminacare.ui.ViewModelFactory
import com.capstone.feminacare.ui.auth.login.LoginActivity
import com.capstone.feminacare.ui.bloodcheckup.BloodCheckupResultActivity
import com.capstone.feminacare.ui.chatbot.ChatBotActivity
import com.capstone.feminacare.utils.CAPTURED_IMAGE_URI
import com.capstone.feminacare.utils.COOKIES
import com.capstone.feminacare.utils.ImageUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private var captureImage: Uri? = null
    private lateinit var cookies: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            println("user cookies = ${user.cookies}")

            if (!user.isLogin) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                cookies = user.cookies
            }
        }

        val navView: BottomNavigationView = binding.navView
        navView.background = null
        navView.menu.getItem(2).isEnabled = false

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        bounceFabAnimation()

        binding.floatingActionButton.setOnClickListener {
            startCamera()
        }

        binding.fabChatbot.setOnClickListener {
            val intent = Intent(this@MainActivity, ChatBotActivity::class.java)
            intent.putExtra(COOKIES, cookies)
            startActivity(intent)
        }
    }

    private fun configureScreen() {

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }
    }

    private fun startCamera() {
        captureImage = ImageUtils.getImageUri(this)
        launcherIntentCamera.launch(captureImage)
    }

    private fun bounceFabAnimation() {
        val fab = binding.fabChatbot
        val bounceAnimator = ObjectAnimator.ofFloat(fab, "translationY", 20f, 0f)
        bounceAnimator.duration = 500 // Adjust the duration as needed
        bounceAnimator.interpolator = SpringInterpolator(0.3, 1.0)
        bounceAnimator.repeatCount = ObjectAnimator.INFINITE
        bounceAnimator.repeatMode = ObjectAnimator.REVERSE

        // Start the animation
        bounceAnimator.start()
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val intent = Intent(this, BloodCheckupResultActivity::class.java)
            intent.putExtra(CAPTURED_IMAGE_URI, captureImage.toString())
            intent.putExtra(COOKIES, cookies)
            startActivity(intent)
        }
    }

    inner class SpringInterpolator(private val stiffness: Double, private val damping: Double) :
        Interpolator {

        override fun getInterpolation(input: Float): Float {
            val result =
                (1.0 + (-Math.exp(-input / stiffness) * Math.cos(damping * input))).toFloat()
            return result
        }
    }

}