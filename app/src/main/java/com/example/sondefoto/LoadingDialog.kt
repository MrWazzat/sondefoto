package com.example.sondefoto

import android.app.Activity
import android.app.AlertDialog
import androidx.fragment.app.Fragment

class LoadingDialog(private val activity: Activity) : Fragment() {

    lateinit var dialog: AlertDialog

    fun startLoadingAnimation(){
        val builder = AlertDialog.Builder(activity)

        builder.setView(activity.layoutInflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }

    fun displayReplayButton(){
        val button = R.id.retryButton;
    }

}