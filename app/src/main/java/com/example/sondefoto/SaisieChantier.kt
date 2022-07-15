package com.example.sondefoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.example.sondefoto.databinding.ActivitySaisieChantierBinding
import com.example.sondefoto.utils.NumberTextWatcher
import java.util.*

class SaisieChantier : AppCompatActivity() {
    private lateinit var binding: ActivitySaisieChantierBinding
    private lateinit var sessionManager: SessionManager

    private var firstName: String? = null
    private var lastName: String? = null
    private var numChantier: String? = null

    private var datePicker: DatePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaisieChantierBinding.inflate(layoutInflater)

        sessionManager = SessionManager(this)

        firstName = sessionManager.getString(FIRST_NAME_KEY)
        lastName = sessionManager.getString(LAST_NAME_KEY)
        numChantier = sessionManager.getString(NUM_CHANTIER_KEY)

        datePicker = binding.dpDateProduction
        datePicker!!.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

        binding.mbName.text = String.format("%s %s", firstName, lastName)
        binding.etNumeroChantier.setText(numChantier)
        
        binding.mbName.setOnClickListener{
            openNameSettings()
        }

        val etNumeroChantier = binding.etNumeroChantier
        etNumeroChantier.transformationMethod = null
        etNumeroChantier.addTextChangedListener(NumberTextWatcher(etNumeroChantier))

        binding.btnContinue.setOnClickListener {
            if(validateChantierField(etNumeroChantier)){
                saveNumChantier()
                saveProductionDate()
                openPictureView()
            }
        }

        setContentView(binding.root)
    }

    private fun validateChantierField(etNumeroChantier: EditText): Boolean {
        if(etNumeroChantier.text.length < 7){
            etNumeroChantier.setError("Format Incorrect")
            return false
        }
        return true
    }

    private fun saveProductionDate() {
        val productionDate = binding.dpDateProduction.getFormattedDate()
        sessionManager.saveString(PRODUCTION_DATE_KEY, productionDate)
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

    fun DatePicker.getFormattedDate(): String {
        return "${dayOfMonth.toString().padStart(2,'0')}/${(month+1).toString().padStart(2,'0')}/$year"
    }
}