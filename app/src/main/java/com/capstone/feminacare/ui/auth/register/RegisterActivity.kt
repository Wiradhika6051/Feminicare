package com.capstone.feminacare.ui.auth.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.capstone.feminacare.databinding.ActivityRegisterBinding
import com.capstone.feminacare.ui.ViewModelFactory
import com.capstone.feminacare.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        viewModel.successAlert.observe(this) { successMessage ->
            if (!successMessage.isNullOrEmpty()) {
                showAlert1(successMessage)
            }
        }
        viewModel.errorAlert.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showAlert2(errorMessage)
            }
        }

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun register() {
        val username = binding.usernameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val first = binding.firstnameEditText.text.toString().trim()
        val last = binding.lastnameEditText.text.toString().trim()

        viewModel.registerUser(username, email, password, first, last)
    }

    private fun showAlert1(message: String) {
        AlertDialog.Builder(this@RegisterActivity).apply {
            setMessage(message)
            setPositiveButton("Lanjut") { _,  _->
                finish()
            }
            create()
            show()
        }
    }

    private fun showAlert2(message: String) {
        AlertDialog.Builder(this@RegisterActivity).apply {
            setMessage(message)
            setPositiveButton("OK") { _,  _->
            }
            create()
            show()
        }
    }

    private fun setupAction() {

        binding.registerButton.setOnClickListener {
            register()
        }

        binding.LoginRoute.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}