package com.example.sondefoto

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sondefoto.communication.SondeforMailer
import com.example.sondefoto.databinding.ActivityPicturePreviewBinding
import com.example.sondefoto.databinding.ActivitySaisieChantierBinding

class PicturePreview : AppCompatActivity() {

    private lateinit var binding: ActivityPicturePreviewBinding

    private lateinit var toSaisieChantier: Intent

    private val mailer = SondeforMailer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPicturePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toSaisieChantier = Intent(this, SaisieChantier::class.java)

        var imagePath = getIntent().getStringExtra("IMAGE_PATH")
        val bitmap = BitmapFactory.decodeFile(imagePath)

        binding.imageView.setImageBitmap(bitmap)
        binding.btnValidate.setOnClickListener {
            if (imagePath != null) {
                mailer.sendMail(imagePath)
                startActivity(toSaisieChantier)
            }
        }

        binding.btnCancel.setOnClickListener {
            startActivity(toSaisieChantier)
        }
    }
}