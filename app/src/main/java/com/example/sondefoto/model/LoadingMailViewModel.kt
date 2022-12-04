package com.example.sondefoto.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.security.auth.callback.Callback

class LoadingMailViewModel: ViewModel(){
    private val mutableState = MutableLiveData<DialogState>()
    val state: LiveData<DialogState> get() = mutableState

    private lateinit var callback: () -> Unit

    fun setState(newState: DialogState){
        mutableState.value = newState
    }

    fun setCallback(callback: () -> Unit){
        this.callback = callback;
    }

    fun getCallback(): () -> Unit{
        return callback
    }
}