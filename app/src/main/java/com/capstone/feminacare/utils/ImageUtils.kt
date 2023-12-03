package com.capstone.feminacare.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.capstone.feminacare.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {
    private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    private var tempImageFile: File? = null

//    fun getImageUri(context: Context): Uri {
//        var uri: Uri? = null
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            val contentValues = ContentValues().apply {
//                put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
//                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
//            }
//            uri = context.contentResolver.insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                contentValues
//            )
//        }
//        return uri ?: getImageUriForPreQ(context)
//    }

    fun getImageUri(context: Context): Uri {
        // Create a temporary file for storing the image
        tempImageFile = createCustomTempFile(context)

        // Get the URI from the temporary file
        return FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                tempImageFile!!
            )
        }


    fun deleteCapturedImage() {
        // Delete the temporary file if it exists
        tempImageFile?.delete()
    }


    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

}