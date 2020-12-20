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
import com.bumptech.glide.Glide

class FragmentMoviesDetails : Fragment(R.layout.fragment_movie_details) {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private var listener: TransactionsFragmentClicks? = null
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
            binding.tvMovieTitle.text = it.title
            binding.tvMovieDescription.text = it.genres.joinToString()
            binding.ratingBar.rating = it.ratings
            binding.tvAge.text = it.minimumAge.toString()+"+"
            binding.frTvMovieReview.text = it.numberOfRatings.toString()
            binding.tvDetails.text = it.overview
            if (it.actors.isNotEmpty()) {
                loadActors(it.actors)
            } else {
                binding.tvCast.visibility = View.INVISIBLE
            }

            Glide
                .with(requireContext())
                .load(it.backdrop)
                .into(binding.ivBackground)

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

    private fun loadActors(myActors: List<Actor>) {
        actorAdapter.addItems(myActors)
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