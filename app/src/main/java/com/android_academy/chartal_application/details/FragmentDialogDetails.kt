package com.android_academy.chartal_application.details

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android_academy.chartal_application.databinding.FragmentDialogDetailsBinding


class FragmentDialogDetails(private val listener: onClickSaveListener) : DialogFragment() {

    private var _binding: FragmentDialogDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOk.setOnClickListener {
            listener.saveData()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface  onClickSaveListener{
        fun saveData()
    }

}