package com.android_academy.chartal_application.details

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentDialogDetailsBinding
import com.android_academy.chartal_application.room.UserDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class FragmentDialogDetails(val movie: Movie?) : DialogFragment() {

    private var _binding: FragmentDialogDetailsBinding? = null
    private val binding get() = _binding!!

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }
    private var scope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main +
                exceptionHandler
    )

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
            Toast.makeText(context, "Movie saved in database", Toast.LENGTH_SHORT).show()
            scope.launch {
                withContext(IO) {
                    val db = UserDatabase.getDatabase(requireContext())
                    db.filmDao().insert(movie)
                }
            }
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
}