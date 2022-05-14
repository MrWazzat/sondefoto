package com.example.sondefoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sondefoto.databinding.ActivitySaisieChantierBinding
import com.example.sondefoto.utils.NumberTextWatcher

class SaisieChantier : AppCompatActivity() {
    private lateinit var binding: ActivitySaisieChantierBinding
    private lateinit var sessionManager: SessionManager

    private var firstName: String? = null
    private var lastName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaisieChantierBinding.inflate(layoutInflater)

        sessionManager = SessionManager(this)

        firstName = sessionManager.getString(FIRST_NAME_KEY)
        lastName = sessionManager.getString(LAST_NAME_KEY)

        binding.mbName.text = String.format("%s %s", firstName, lastName)

        binding.mbName.setOnClickListener{
            openNameSettings()
        }

        val et = binding.etNumeroChantier
        et.transformationMethod = null
        et.addTextChangedListener(NumberTextWatcher(et))

        binding.btnContinue.setOnClickListener {
            saveNumChantier()
            openPictureView()
        }

        setContentView(binding.root)
    }

    private fun saveNumChantier(){
        val numeroChantier = binding.etNumeroChantier.text.toString()
        sessionManager.saveString(NUM_CHANTIER_KEY, numeroChantier)
    }

    private fun openPictureView(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

    private fun openNameSettings(){
        val intent = Intent(this, NameSettings::class.java)
        intent.putExtra("FORCE_OPEN", true);
        startActivity(intent);
    }
}