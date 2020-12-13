package com.android_academy.chartal_application.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android_academy.chartal_application.adapters.ActorAdapter
import com.android_academy.chartal_application.adapters.MovieAdapter
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentMoviesListBinding
import com.android_academy.chartal_application.repository.DataStorage


class FragmentMoviesList : Fragment(), MovieAdapter.Listener {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private var listener: TransactionsFragmentClicks? = null
    private val movieAdapter by lazy{
        MovieAdapter(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null && activity is TransactionsFragmentClicks) {
            listener = activity as TransactionsFragmentClicks
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMovies?.apply {
            adapter = movieAdapter
            setHasFixedSize(true)
        }
        loadMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun oItemClicked(movie: Movie) {
        listener?.addFragmentMoviesDetails(movie)
    }

    private fun loadMovies() {
        movieAdapter.addItems(DataStorage.getMoviesList())
    }

}