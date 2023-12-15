package com.capstone.feminacare.ui.auth.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.capstone.feminacare.databinding.ActivityRegisterBinding
import com.capstone.feminacare.ui.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val registerViewModel: RegisterViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
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

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailTextView.text.toString()
            val password = binding.passwordEditText.text.toString()
            val first_name = binding.firstnameEditText.text.toString()
            val last_name = binding.lastnameEditText.text.toString()

            registerViewModel.registerUser(username, email, password, first_name, last_name))
                .observe(this@RegisterActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                loading(true)
                            }

                            is Result.Success -> {
                                loading(false)
                                val registerData = result.data
                                Snackbar.make(
                                    window.decorView.rootView,
                                    registerData.message.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, RegisterActivity::class.java))
                            }

                            is Result.Error -> {
                                loading(false)
                                val errorMessage = result.error
                                Snackbar.make(
                                    window.decorView.rootView, errorMessage, Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    private fun loading(isLoading: Boolean) {
        with(binding) {
            registerButton.isEnabled = !isLoading
            emailTextView.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            firstnameEditText.isEnabled = !isLoading
            lastnameEditText.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}