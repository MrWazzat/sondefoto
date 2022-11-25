package com.example.sondefoto.communication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.example.sondefoto.BuildConfig
import com.example.sondefoto.SaisieChantier

class SondeforMailer private constructor(
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val numeroChantier: String?,
    val productionDate: String?,
    val attachmentPath: String,

    val successCallback: () -> Unit,
    val failureCallback: () -> Unit,
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
            .subject("$firstName $lastName : $numeroChantier ($productionDate) || $phoneNumber")
            .body("Ci-joint, le document lié au chantier $numeroChantier du ${productionDate}. \n" +
                        "Cordialement, \n" +
                        "$firstName $lastName")
            .attachment(attachmentPath)
            .onCompleteCallback(object : MaildroidX.onCompleteCallback {
                override val timeout: Long = 3000
                override fun onSuccess() {
                    Log.d("SondeforMailer","Mail sent successfully")
                    successCallback()
                }

                override fun onFail(errorMessage: String) {
                    Log.d("SondeforMailer","Mail sent successfully")
                    failureCallback()
                }
            })
            .mail()
        Log.i("SondeforMailer", "Mail SENT")
    }

    data class Builder(
        var firstName: String? = null,
        var lastName: String? = null,
        var phoneNumber: String? = null,
        var numeroChantier: String? = null,
        var productionDate: String? = null,
        var attachmentPath: String = "",
        var successCallback: () -> Unit = {},
        var failureCallback: () -> Unit = {} ) {

        fun firstName(firstName: String) = apply { this.firstName = firstName }
        fun lastName(lastName: String) = apply { this.lastName = lastName }
        fun phoneNumber(phoneNumber: String) = apply { this.phoneNumber = phoneNumber }
        fun numeroChantier(numeroChantier: String) = apply { this.numeroChantier = numeroChantier }
        fun productionDate(productionDate: String) = apply { this.productionDate = productionDate }
        fun attachmentPath(attachmentPath: String) = apply { this.attachmentPath = attachmentPath }
        fun successCallback(successCallback: () -> Unit) = apply { this.successCallback = successCallback }
        fun failureCallback(failureCallback: () -> Unit) = apply { this.failureCallback = failureCallback }
        fun build() = SondeforMailer(firstName, lastName, phoneNumber, numeroChantier, productionDate, attachmentPath, successCallback, failureCallback)
    }
}