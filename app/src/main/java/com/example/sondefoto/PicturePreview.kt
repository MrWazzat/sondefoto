package com.example.sondefoto

import android.annotation.SuppressLint
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
    private lateinit var sessionmanager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPicturePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionmanager = SessionManager(this)

        toSaisieChantier = Intent(this, SaisieChantier::class.java, )

        var imagePath = intent.getStringExtra("IMAGE_PATH")
        val bitmap = BitmapFactory.decodeFile(imagePath)

        binding.imageView.setImageBitmap(bitmap)
        binding.tvNumChantier.text = String.format("Chantier n° %s", sessionmanager.getString(NUM_CHANTIER_KEY))

        val loadingDialog = LoadingDialog(this)

        binding.btnValidate.setOnClickListener {
            loadingDialog.startLoadingAnimation()
            if (imagePath != null) {
                SondeforMailer.Builder()
                    .firstName(sessionmanager.getString(FIRST_NAME_KEY).orEmpty())
                    .lastName(sessionmanager.getString(LAST_NAME_KEY).orEmpty())
                    .numeroChantier(sessionmanager.getString(NUM_CHANTIER_KEY).orEmpty())
                    .productionDate(sessionmanager.getString(PRODUCTION_DATE_KEY).orEmpty())
                    .attachmentPath(imagePath)
                    .build()
                    .sendMail(applicationContext)
            }
        }

        binding.btnCancel.setOnClickListener {
            startActivity(toSaisieChantier)
        }
    }

    fun openSaisieChantier(){
        startActivity(toSaisieChantier)
    }
}