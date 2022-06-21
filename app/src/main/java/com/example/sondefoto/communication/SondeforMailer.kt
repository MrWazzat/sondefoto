package com.example.sondefoto.communication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.example.sondefoto.BuildConfig
import com.example.sondefoto.MainActivity
import com.example.sondefoto.R
import com.example.sondefoto.SaisieChantier

class SondeforMailer private constructor(
    val firstName: String?,
    val lastName: String?,
    val numeroChantier: String?,
    val productionDate: String?,
    val attachmentPath: String
){

    private val SMTP_SERVER = "email.sondefor.fr"
    private val SMTP_PORT = "587"

    private val SMTP_USERNAME = "test@sondefor.fr"
    private val SMTP_PASSWORD = "!77OgQdmh93s"

    fun sendMail(context: Context) {
        MaildroidX.Builder()
            .smtp(SMTP_SERVER)
            .smtpUsername(SMTP_USERNAME)
            .smtpPassword(SMTP_PASSWORD)
            .port(SMTP_PORT)
            .type(MaildroidXType.HTML)
            .to(BuildConfig.RECEIVER_EMAIL)
            .from(BuildConfig.SENDER_EMAIL)
            .subject("$firstName $lastName : $numeroChantier ($productionDate)")
            .body("Ci-joint, le document lié au chantier $numeroChantier du ${productionDate}. \n" +
                        "Cordialement, \n" +
                        "$firstName $lastName")
            .attachment(attachmentPath)
            .onCompleteCallback(object : MaildroidX.onCompleteCallback {
                override val timeout: Long = 3000
                override fun onSuccess() {
                    Log.d("MaildroidX", "SUCCESS")
                    Toast.makeText(context, "Mail envoyé avec succès", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, SaisieChantier::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }

                override fun onFail(errorMessage: String) {
                    Log.d("MaildroidX", "FAIL")
                    Toast.makeText(context, "Erreur lors de l'envoi du mail", Toast.LENGTH_SHORT).show()
                }
            })
            .mail()
        Log.i("SondeforMailer", "Mail SENT")
    }

    data class Builder(
        var firstName: String? = null,
        var lastName: String? = null,
        var numeroChantier: String? = null,
        var productionDate: String? = null,
        var attachmentPath: String = "") {

        fun firstName(firstName: String) = apply { this.firstName = firstName }
        fun lastName(lastName: String) = apply { this.lastName = lastName }
        fun numeroChantier(numeroChantier: String) = apply { this.numeroChantier = numeroChantier }
        fun productionDate(productionDate: String) = apply { this.productionDate = productionDate }
        fun attachmentPath(attachmentPath: String) = apply { this.attachmentPath = attachmentPath }
        fun build() = SondeforMailer(firstName, lastName, numeroChantier, productionDate, attachmentPath)
    }
}