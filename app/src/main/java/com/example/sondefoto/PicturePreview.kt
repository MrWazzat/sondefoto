package com.example.sondefoto

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sondefoto.databinding.ActivityPicturePreviewBinding
import com.example.sondefoto.databinding.ActivitySaisieChantierBinding

class PicturePreview : AppCompatActivity() {

    private lateinit var binding: ActivityPicturePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPicturePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var imagePath = getIntent().getStringExtra("IMAGE_PATH")
        val bitmap = BitmapFactory.decodeFile(imagePath)

        binding.imageView.setImageBitmap(bitmap)
    }
}