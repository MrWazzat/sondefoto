package com.example.sondefoto

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.opengl.ETC1.getWidth
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sondefoto.communication.SondeforMailer
import com.example.sondefoto.databinding.ActivityPicturePreviewBinding
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths


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

        var phoneNumber = "?????????"

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun persistImageToPicturesDirectory(bitmap: Bitmap){
        // ERROR : java.io.filenotfoundexception /storage/emulated/0/
        ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE), 1)
        try{
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/" + sondeforDirectoryName
            val newFile = createFile(directory, System.currentTimeMillis().toString() + ".jpg")


            val outputStream = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush();
            outputStream.getFD().sync();
            outputStream.close();

            MediaScannerConnection.scanFile(this, arrayOf(newFile.getAbsolutePath()), null, null);
        }catch (e: Exception){
            Toast.makeText(this, "Erreur lors de l'enregistrement de la photo", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createFile(directoryPath: String, fileName: String): File{
        Files.createDirectories(Paths.get(directoryPath))

        val newFile = File(directoryPath, fileName)

        if(!newFile.exists()){
            newFile.createNewFile()
        }

        return newFile
    }

    fun openSaisieChantier(){
        startActivity(toSaisieChantier)
    }
}