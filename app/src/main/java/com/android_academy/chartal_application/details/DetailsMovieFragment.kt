package com.android_academy.chartal_application.details

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.android_academy.chartal_application.App
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.adapters.ActorAdapter
import com.android_academy.chartal_application.broadcast.ReminderReceiver
import com.android_academy.chartal_application.data.Actor
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentMovieDetailsBinding
import com.android_academy.chartal_application.repository.NetworkModule
import com.android_academy.chartal_application.util.NetworkStatus
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


class DetailsMovieFragment() : Fragment(R.layout.fragment_movie_details),
    DialogDatabaseFragment.IClickListener {
    private val networkStatus = NetworkStatus(App.instance)

    private val detailsViewModel: DetailsViewModel by viewModels {
        DetailsMovieViewModelFactory(
            NetworkModule.filmsRepository,
            networkStatus
        )
    }
    private var movie: Movie? = null
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private var listener: TransactionsFragmentClicks? = null
    private var movieId = 0
    private val actorAdapter by lazy {
        ActorAdapter()
    }
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var isRationaleShown = false

    @SuppressLint("MissingPermission")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null && activity is TransactionsFragmentClicks) {
            listener = activity as TransactionsFragmentClicks
        }
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onCalendarPermissionGranted()

            } else {
                onCalendarPermissionNotGranted()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvBack.setOnClickListener {
            listener?.addFragmentMoviesList()
        }
        binding.rvDetails.adapter = actorAdapter
        this.arguments?.getParcelable<Movie>(ARGS_MOVIE)?.let {
            movie = it
            movieId = it.id
            binding.tvMovieTitle.text = it.title
            binding.tvMovieDescription.text = it.genres?.joinToString()
            binding.ratingBar.rating = it.ratings
            binding.tvAge.text = it.minimumAge.toString() + "+"
            binding.frTvMovieReview.text = it.numberOfRatings.toString()
            binding.tvDetails.text = it.overview
            binding.ivBackground.load(it.backdrop) {
                placeholder(R.drawable.chartal_placeholder)
            }
            if (savedInstanceState == null) {
                detailsViewModel.loadActors(it.id)
            } else {
                detailsViewModel.loadActorsFromCache(it.id)
            }
        }
        restorePreferencesData()
        val flag = this.arguments?.getBoolean(FLAG)
        detailsViewModel.actors.observe(viewLifecycleOwner, Observer { list ->
            loadActors(list)
            if (list.isEmpty()) {
                binding.tvCast.visibility = View.INVISIBLE
            } else {
                binding.tvCast.visibility = View.VISIBLE
            }
        })
        detailsViewModel.trailerUrl.observe(viewLifecycleOwner, Observer { trailer ->
            openMovie(trailer)
        })
        binding.fab.setOnClickListener {
            detailsViewModel.getTrailer(movieId)
        }
        binding.btnPermission?.setOnClickListener {
            onSendCalendar()
        }

        initErrorHandler()

        if (flag!!) {
            binding.btnDialog.setImageResource(R.drawable.ic_baseline_save_24)
        } else {
            binding.btnDialog.setImageResource(R.drawable.ic_baseline_delete_24)
        }
        binding.btnDialog.setOnClickListener {
            val dialog = DialogDatabaseFragment.newInstance(flag)
            dialog.show(childFragmentManager, "FragmentDialogDetails")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        savePreferencesData()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        requestPermissionLauncher.unregister()
    }

    override fun saveData() {
        Toast.makeText(context, "Movie saved in database", Toast.LENGTH_SHORT).show()
        detailsViewModel.saveUserMovie(movie)
    }

    override fun deleteData() {
        Toast.makeText(context, "The movie was deleted from the database", Toast.LENGTH_SHORT)
            .show()
        detailsViewModel.deleteUserMovie(movie)
    }

    private fun loadActors(myActors: List<Actor>) {
        actorAdapter.addItems(myActors)
    }

    private fun openMovie(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun initErrorHandler() {
        detailsViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun savePreferencesData() {
        activity?.let {
            it.getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN, isRationaleShown)
                .apply()
        }
    }

    private fun restorePreferencesData() {
        isRationaleShown = activity?.getPreferences(Context.MODE_PRIVATE)?.getBoolean(
            KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN,
            false
        ) ?: false
    }

    private fun onSendCalendar() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CALENDAR)
                        == PackageManager.PERMISSION_GRANTED -> onCalendarPermissionGranted()
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR) ->
                    showCalendarPermissionExplanationDialog()
                isRationaleShown -> showCalendarPermissionDeniedDialog()
                else -> requestCalendarPermission()
            }
        }
    }

    private fun requestCalendarPermission() {
        context?.let {
            requestPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)
        }
    }

    @RequiresPermission(Manifest.permission.READ_CALENDAR)
    private fun onCalendarPermissionGranted() {
        context?.let {
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.show(childFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener { data ->
                val calendar = Calendar.getInstance()
                val dateStart = calendar.timeInMillis
                val triggerTimeInMillis = data - dateStart
                val time = System.currentTimeMillis() + triggerTimeInMillis
                val triggerTimeInDays = (triggerTimeInMillis / (1000 * 60 * 60 * 24)).toInt()
                val intent = Intent(context, ReminderReceiver::class.java).apply {
                    putExtra(MOVIE_ID, movie?.id)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    REQUEST_CONTENT,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val alarmManager = App.instance.getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
                val message = when (triggerTimeInDays) {
                    0 -> "The reminder will work soon"
                    else -> "The reminder will work after ${triggerTimeInDays + 1} days"
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onCalendarPermissionNotGranted() {
        context?.let {
            Toast.makeText(context, "Пермишена на календарь нету", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCalendarPermissionExplanationDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage("To continue please grant access to calendar")
                .setPositiveButton("OK") { dialog, _ ->
                    isRationaleShown = true
                    requestCalendarPermission()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showCalendarPermissionDeniedDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage("This feature is not available without calendar permissions. Open app settings?")
                .setPositiveButton("OK") { dialog, _ ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + it.packageName)
                        )
                    )
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun showCalendarProviderSettingsDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage("Location providers disabled. Open device settings?")
                .setPositiveButton("OK") { dialog, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }


    companion object {
        private const val ARGS_MOVIE = "ARGS_MOVIE"
        private const val FLAG = "BTN_FLAG"
        private const val KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN =
            "KEY_CALENDAR_PERMISSION_RATIONALE_SHOWN_APP"
        private const val REQUEST_CONTENT = 1
        const val MOVIE_ID = "MOVIE_ID"
        fun newInstance(movie: Movie, flag: Boolean): DetailsMovieFragment {
            return DetailsMovieFragment().apply {
                arguments = bundleOf(ARGS_MOVIE to movie, FLAG to flag)
            }
        }
    }
}