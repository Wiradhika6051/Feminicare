package com.capstone.feminacare.ui.bloodcheckup

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.feminacare.R
import com.capstone.feminacare.data.Result
import com.capstone.feminacare.data.local.BloodCheckup
import com.capstone.feminacare.databinding.ActivityBloodCheckupResultBinding
import com.capstone.feminacare.ui.CheckupViewModelFactory
import com.capstone.feminacare.utils.BloodIndex
import com.capstone.feminacare.utils.CAPTURED_IMAGE_URI
import com.capstone.feminacare.utils.ColorIndex
import com.capstone.feminacare.utils.ImageUtils
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class BloodCheckupResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodCheckupResultBinding
    private val viewModel by viewModels<BloodCheckupViewModel> {
        CheckupViewModelFactory.getInstance(this)
    }
    private var currentImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBloodCheckupResultBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        Setup Toolbar
        val toolbar: MaterialToolbar = binding.materialToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getCaptureResult()
    }

    private fun getCaptureResult() {
        val captureResult = intent.getStringExtra(CAPTURED_IMAGE_URI)
        val imageUri = Uri.parse(captureResult)
        currentImage = imageUri
        binding.ivCameraCapture.setImageURI(imageUri)
        postPhoto()
    }

    private fun postPhoto() {
        currentImage?.let {
            val imageFile = ImageUtils.uriToFile(it, this)
            val requestImage = imageFile.asRequestBody("image/jpeg".toMediaType())

            val multipartBody = MultipartBody.Part.createFormData(
                "menstrualBloodImage",
                imageFile.name,
                requestImage
            )

            viewModel.postPhoto(multipartBody).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        lifecycleScope.launch {
                            delay(2000L)
                            showLoading(false)
                        }
                        println(result.data)

                        val colorIndex =
                            ColorIndex.fromInt(result.data.data.colorIndex) as ColorIndex
                        val bloodIndex = BloodIndex(colorIndex, null)
                        val descResource = getString(bloodIndex.getDescription()).trimIndent()
                        val healthInfo = if (bloodIndex.isHealthy == true) {
                            "Healthy"
                        } else {
                            "Unhealthy"
                        }

                        val newCheckup = BloodCheckup(
                            timeStamp = System.currentTimeMillis(),
                            healthInfo = healthInfo,
                            description = descResource
                        )

                        binding.tvCheckupResult.setText(bloodIndex.getDescription())
                        binding.tvHealthy.text = healthInfo

                        binding.tvHealthy.setTextColor(
                            when (bloodIndex.isHealthy) {
                                true -> ContextCompat.getColor(this, R.color.custom_pink)
                                else -> ContextCompat.getColor(this, R.color.orange)
                            }
                        )

                        viewModel.insertBloodCheckup(newCheckup)
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            this,
                            "Failed to getting the result! : ${result.error}",
                            Toast.LENGTH_LONG
                        ).show()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(Toast.LENGTH_LONG.toLong())
                            finish()
                        }

                    }
                }

            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.loadingIndicator.root.visibility =
            View.VISIBLE else binding.loadingIndicator.root.visibility = View.GONE
    }

    override fun onDestroy() {
        currentImage?.let {
            ImageUtils.deleteCapturedImage()
        }
        super.onDestroy()
    }
}