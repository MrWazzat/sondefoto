package com.example.sondefoto

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.sondefoto.databinding.ActivityNameSettingsBinding
import com.example.sondefoto.databinding.LoadingDialogBinding
import com.example.sondefoto.model.DialogState
import com.example.sondefoto.model.LoadingMailViewModel
import org.w3c.dom.Text

class LoadingDialogFragment : DialogFragment(R.layout.loading_dialog){

    private var _binding: LoadingDialogBinding? = null

    private val binding get() = _binding!!
    private val viewModel: LoadingMailViewModel by activityViewModels()

    private lateinit var replayButton: Button
    private lateinit var iconImage: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var textContent: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoadingDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        replayButton = binding.retryButton
        iconImage = binding.statusIcon
        progressBar = binding.progressBar
        textContent = binding.textView

        replayButton.setOnClickListener {
            resetState()
            viewModel.getCallback()()
        }

        viewModel.state.observe(this) { state ->
            when (state) {
                DialogState.LOADING -> Log.d("LoadingDialogFragment", state.toString())
                DialogState.SUCCESS -> success()
                DialogState.FAILED -> error()
                else -> {}
            }
        }

        return view
    }

    fun error(){
        replayButton.visibility = View.VISIBLE
        iconImage.visibility = View.VISIBLE

        progressBar.visibility = View.INVISIBLE
        textContent.text = "Erreur lors de l'envoi du mail"
        iconImage.setBackgroundResource(R.drawable.error_icon)
    }

    fun success(){
        progressBar.visibility = View.INVISIBLE

        textContent.text = "Mail envoyé avec succès !"
        iconImage.setBackgroundResource(R.drawable.ok_icon)
        iconImage.visibility = View.VISIBLE
    }

    fun resetState(){
        replayButton.visibility = View.GONE
        iconImage.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        textContent.text = "Envoi du mail en cours..."
    }
}