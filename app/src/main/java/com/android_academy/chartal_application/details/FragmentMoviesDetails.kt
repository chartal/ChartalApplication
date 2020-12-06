package com.android_academy.chartal_application.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.adapters.ActorAdapter
import com.android_academy.chartal_application.data.Actor
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentMovieDetailsBinding

class FragmentMoviesDetails : Fragment(R.layout.fragment_movie_details) {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private var listener: TransactionsFragmentClicks? = null
    private val actorAdapter = ActorAdapter()

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
        binding.rvDetails?.adapter = actorAdapter

        this.arguments?.getParcelable<Movie>(ARGS_MOVIE)?.let {
            binding.tvMovieTitle.text = it.title
            binding.tvMovieDescription.text = it.description
            binding.ratingBar.rating = it.rating
            binding.tvAge.text = it.age
            binding.ivBackground.setImageResource(it.backdropRes)
            binding.frTvMovieReview.text = it.review
            binding.tvDetails.text = it.overview
            loadActors(it.listActors)
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

    private fun loadActors(actors: List<Actor>) {
        actorAdapter.addItems(actors)
    }

    companion object {
        private const val ARGS_MOVIE = "ARGS_MOVIE"
        fun newInstance(movie: Movie): FragmentMoviesDetails {
            return FragmentMoviesDetails().apply {
                arguments = bundleOf(ARGS_MOVIE to movie)
            }
        }
    }

}