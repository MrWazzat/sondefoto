package com.example.sondefoto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sondefoto.databinding.ActivityNameSettingsBinding

class NameSettings : AppCompatActivity() {
    private lateinit var binding: ActivityNameSettingsBinding
    private lateinit var sessionmanager: SessionManager

    private lateinit var etFirstName : EditText
    private lateinit var etLastName : EditText
    override fun onCreate(savedInstanceState: Bundle?) {

        val forceOpen = intent.getBooleanExtra("FORCE_OPEN", false)

        super.onCreate(savedInstanceState)

        sessionmanager = SessionManager(this)

        if(isNameAlreadySetInSession() && !forceOpen){
            openSaisieChantierActivity()
        }

        binding = ActivityNameSettingsBinding.inflate(layoutInflater)
        val view = binding.root

        etFirstName = binding.etFirstName
        etLastName = binding.etLastName

        binding.buttonStart.setOnClickListener {
            if(validateNameFields()){
                saveNameSettings()
                openSaisieChantierActivity()
            }
        }

        setContentView(view)
    }

    private fun validateNameFields(): Boolean{
        val isFirstNameSet = checkEmptyField(etFirstName, "Le prénom doit être renseigné")
        val isLastNameSet = checkEmptyField(etLastName, "Le nom doit être renseigné")
        return isFirstNameSet && isLastNameSet
    }

    private fun checkEmptyField(field: EditText, error: String): Boolean{
        if(field.text.isNullOrBlank()){
            field.setError(error)
            return false
        }

        return true
    }

    private fun isNameAlreadySetInSession(): Boolean{
        return !sessionmanager.getString(FIRST_NAME_KEY).isNullOrEmpty() || !sessionmanager.getString(
            LAST_NAME_KEY).isNullOrEmpty()
    }

    private fun openSaisieChantierActivity() {
        val intent = Intent(this, SaisieChantier::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveNameSettings(){
        sessionmanager.saveString(FIRST_NAME_KEY, etFirstName.text.toString())
        sessionmanager.saveString(LAST_NAME_KEY, etLastName.text.toString())
        Log.i("SAVED NAME", sessionmanager.getString(FIRST_NAME_KEY)!!)
    }
}