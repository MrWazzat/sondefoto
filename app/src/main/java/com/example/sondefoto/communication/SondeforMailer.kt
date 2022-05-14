package com.example.sondefoto.communication

import android.util.Log
import android.widget.Toast
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType

class SondeforMailer {

    fun sendMail(attachmentPath: String) {
        MaildroidX.Builder()
            .smtp("email.sondefor.fr")
            .smtpUsername("test@sondefor.fr")
            .smtpPassword("!77OgQdmh93s")
            .port("587")
            .type(MaildroidXType.HTML)
            .to("raphael.marzat@gmail.com")
            .from("test@sondefor.fr")
            .subject("android studio test")
            .body("This is a simple test without attachments")
            .attachment(attachmentPath)
            .onCompleteCallback(object : MaildroidX.onCompleteCallback {
                override val timeout: Long = 3000
                override fun onSuccess() {
                    Log.d("MaildroidX", "SUCCESS")
                }

                override fun onFail(errorMessage: String) {
                    Log.d("MaildroidX", "FAIL")
                }
            })
            .mail()
        Log.i("SondeforMailer", "Mail SENT")
    }
}