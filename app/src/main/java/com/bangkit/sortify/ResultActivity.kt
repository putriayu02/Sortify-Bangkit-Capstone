package com.bangkit.sortify

import android.content.Intent
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.sortify.databinding.ActivityMainBinding
import com.bangkit.sortify.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvResult.text=intent.getStringExtra("data")
        var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(intent.getStringExtra("image")))
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,150,150)
        binding.ivBarang.setImageBitmap(bitmap)
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        super.onBackPressed()
    }
}