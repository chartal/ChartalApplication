package com.android_academy.chartal_application.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android_academy.chartal_application.adapters.MovieAdapter
import com.android_academy.chartal_application.App
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentMoviesListBinding
import com.android_academy.chartal_application.util.ResProvider

class FragmentMoviesList : Fragment(), MovieAdapter.Listener {

    private val resProvider = ResProvider(App.instance)
    private val detailsViewModel: DetailsViewModel by viewModels { DetailsViewModelFactory(resProvider) }
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private var listener: TransactionsFragmentClicks? = null
    private val movieAdapter by lazy {
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
        binding.rvMovies.apply {
            adapter = movieAdapter
        }
        initErrorHandler()
        loadFilms()
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

    private fun initErrorHandler() {
        detailsViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun loadFilms() {
        detailsViewModel.items.observe(viewLifecycleOwner, Observer {
            movieAdapter.addItems(it!!)
        })
    }
}