package com.android_academy.chartal_application.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.android_academy.chartal_application.App
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.adapters.ActorAdapter
import com.android_academy.chartal_application.data.Actor
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentMovieDetailsBinding
import com.android_academy.chartal_application.repository.NetworkModule
import com.android_academy.chartal_application.util.NetworkStatus


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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null && activity is TransactionsFragmentClicks) {
            listener = activity as TransactionsFragmentClicks
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
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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

    companion object {
        private const val ARGS_MOVIE = "ARGS_MOVIE"
        private const val FLAG = "BTN_FLAG"
        fun newInstance(movie: Movie, flag: Boolean): DetailsMovieFragment {
            return DetailsMovieFragment().apply {
                arguments = bundleOf(ARGS_MOVIE to movie, FLAG to flag)
            }
        }
    }

}