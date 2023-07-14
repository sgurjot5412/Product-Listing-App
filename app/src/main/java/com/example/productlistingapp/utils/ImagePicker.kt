package com.example.productlistingapp.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher

object ImagePicker {
    fun pickImage(activity: Activity, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        launcher.launch(intent)
    }
}
