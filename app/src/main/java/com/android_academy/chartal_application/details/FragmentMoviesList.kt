package com.android_academy.chartal_application.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android_academy.chartal_application.adapters.MovieAdapter
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.data.loadMovies
import com.android_academy.chartal_application.databinding.FragmentMoviesListBinding
import kotlinx.coroutines.*


class FragmentMoviesList : Fragment(), MovieAdapter.Listener {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private var listener: TransactionsFragmentClicks? = null
    private val movieAdapter by lazy{
        MovieAdapter(requireContext(), this)
    }
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
        scope.launch { movieAdapter.addItems(loadMovies(requireContext())) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun oItemClicked (movie: Movie) {
        listener?.addFragmentMoviesDetails(movie)
    }


}