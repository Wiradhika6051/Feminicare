package com.capstone.feminacare.ui.bloodcheckup

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.feminacare.data.Result
import com.capstone.feminacare.databinding.ActivityBloodCheckupResultBinding
import com.capstone.feminacare.ui.ViewModelFactory
import com.capstone.feminacare.utils.CAPTURED_IMAGE_URI
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
        ViewModelFactory.getInstance(this)
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
                when(result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(2000L)
                            showLoading(false)
                        }
                        binding.tvCheckupResult.text = result.data.message
                    }
                    is Result.Error -> {
                        Toast.makeText(this,  "Failed to getting the result! : ${result.error}", Toast.LENGTH_LONG).show()
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
        if(isLoading)  binding.loadingIndicator.root.visibility = View.VISIBLE else  binding.loadingIndicator.root.visibility = View.GONE
    }

    override fun onDestroy() {
        currentImage?.let {
            ImageUtils.deleteCapturedImage()
        }
        super.onDestroy()
    }
}