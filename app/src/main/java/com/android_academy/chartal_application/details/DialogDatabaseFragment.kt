package com.android_academy.chartal_application.details

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.databinding.FragmentDialogDetailsBinding


class DialogDatabaseFragment(

) : DialogFragment() {

    private var _binding: FragmentDialogDetailsBinding? = null
    private val binding get() = _binding!!
    private var flag = true
    private var listener: IClickListener? = null

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
        listener = parentFragment as IClickListener?

        this.arguments?.let {
            flag = it.getBoolean("flag")
        }

        if (flag) {
            binding.tvValue.text = getString(R.string.want_to_save)
        } else {
            binding.tvValue.text = getString(R.string.want_to_delete)
        }
        binding.btnOk.setOnClickListener {
            when (flag) {
                true -> listener?.saveData()
                else -> listener?.deleteData()
            }
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentOrientation = resources.configuration.orientation
        var width = (resources.displayMetrics.widthPixels * 0.60).toInt()
        var height = (resources.displayMetrics.heightPixels * 0.75).toInt()
        if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        }
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface IClickListener {
        fun saveData()
        fun deleteData()
    }

    companion object {
        @JvmStatic
        fun newInstance(flag: Boolean): DialogDatabaseFragment {
            return DialogDatabaseFragment().apply {
                arguments = bundleOf("flag" to flag)
            }
        }
    }
}