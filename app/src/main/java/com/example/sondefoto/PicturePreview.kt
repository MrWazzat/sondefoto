package com.example.sondefoto

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sondefoto.communication.SondeforMailer
import com.example.sondefoto.databinding.ActivityPicturePreviewBinding
import java.io.File
import java.io.FileOutputStream


class PicturePreview : AppCompatActivity() {

    private val sondeforDirectoryName = "SONDEFOR Production/"
    private val permission = 101

    private lateinit var binding: ActivityPicturePreviewBinding

    private lateinit var toSaisieChantier: Intent
    private lateinit var sessionmanager: SessionManager

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPicturePreviewBinding.inflate(layoutInflater)

        // Create obj of TelephonyManager and ask for current telephone service
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var phoneNumber = "?????????"
//        if(ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_SMS
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_PHONE_NUMBERS
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_PHONE_STATE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            phoneNumber = telephonyManager.line1Number
//        }else{
//            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS), permission);
//        }

        setContentView(binding.root)

        sessionmanager = SessionManager(this)

        toSaisieChantier = Intent(this, SaisieChantier::class.java)

        var imagePath = intent.getStringExtra("IMAGE_PATH")
        val bitmap = BitmapFactory.decodeFile(imagePath)

        binding.imageView.setImageBitmap(bitmap)
        binding.tvNumChantier.text = String.format("Chantier n° %s", sessionmanager.getString(NUM_CHANTIER_KEY))

        val loadingDialog = LoadingDialog(this)

        binding.btnValidate.setOnClickListener {
            loadingDialog.startLoadingAnimation()
            if (imagePath != null) {
                persistImageToPicturesDirectory(bitmap)

                SondeforMailer.Builder()
                    .firstName(sessionmanager.getString(FIRST_NAME_KEY).orEmpty())
                    .lastName(sessionmanager.getString(LAST_NAME_KEY).orEmpty())
                    .phoneNumber(phoneNumber)
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

    fun persistImageToPicturesDirectory(bitmap: Bitmap){
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val newFile = File(directory, sondeforDirectoryName + System.currentTimeMillis()+ ".jpg")

        val outputStream = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        outputStream.flush();
        outputStream.getFD().sync();
        outputStream.close();

        MediaScannerConnection.scanFile(this, arrayOf(newFile.getAbsolutePath()), null, null);
    }

    fun openSaisieChantier(){
        startActivity(toSaisieChantier)
    }
}