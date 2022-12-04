package com.example.sondefoto

import android.Manifest
import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.*
import android.os.Build.VERSION_CODES.S
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.sondefoto.communication.SondeforMailer
import com.example.sondefoto.databinding.ActivityPicturePreviewBinding
import com.example.sondefoto.model.DialogState
import com.example.sondefoto.model.LoadingMailViewModel
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Files.createFile
import java.nio.file.Paths


class PicturePreview : AppCompatActivity() {

    private val sondeforDirectoryName = "SONDEFOR Production/"
    private val viewModel: LoadingMailViewModel by viewModels()

    private lateinit var binding: ActivityPicturePreviewBinding

    private lateinit var toSaisieChantier: Intent
    private lateinit var sessionmanager: SessionManager

    private lateinit var imagePath: String
    private lateinit var bitmap: Bitmap
    private lateinit var phoneNumber: String

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPicturePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init variables
        phoneNumber = getPhoneNumber()
        sessionmanager = SessionManager(this)
        toSaisieChantier = Intent(this, SaisieChantier::class.java)
        imagePath = intent.getStringExtra("IMAGE_PATH")!!
        bitmap = BitmapFactory.decodeFile(imagePath)
        binding.imageView.setImageBitmap(bitmap)
        binding.tvNumChantier.text = String.format("Chantier n° %s", sessionmanager.getString(NUM_CHANTIER_KEY))
        viewModel.setCallback { sendEmail() }

        binding.btnValidate.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<LoadingDialogFragment>(R.id.fragment_container_view)
            }

            viewModel.setState(DialogState.LOADING)

            persistImageToPicturesDirectory(bitmap)
            sendEmail()
        }

        binding.btnCancel.setOnClickListener {
            startActivity(toSaisieChantier)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun sendEmail(){
            binding.btnValidate.isEnabled = false
            SondeforMailer.Builder()
                .firstName(sessionmanager.getString(FIRST_NAME_KEY).orEmpty())
                .lastName(sessionmanager.getString(LAST_NAME_KEY).orEmpty())
                .phoneNumber(phoneNumber)
                .numeroChantier(sessionmanager.getString(NUM_CHANTIER_KEY).orEmpty())
                .productionDate(sessionmanager.getString(PRODUCTION_DATE_KEY).orEmpty())
                .attachmentPath(imagePath)
                .successCallback(::successCallback)
                .failureCallback(::failureCallback)
                .build()
                .sendMail()
    }

    fun successCallback() {
        viewModel.setState(DialogState.SUCCESS)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(applicationContext, SaisieChantier::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext.startActivity(intent)
            }, 2500
        )
    }

    fun failureCallback() {
        viewModel.setState(DialogState.FAILED)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun persistImageToPicturesDirectory(bitmap: Bitmap){
        ActivityCompat.requestPermissions(this,  arrayOf(READ_EXTERNAL_STORAGE,
            MANAGE_EXTERNAL_STORAGE), 1)
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

    fun getPhoneNumber(): String{
        val telManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if ( ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE), 100)
            }
        }

        return telManager.line1Number ?: "??????????"
    }
}