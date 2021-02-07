package com.android_academy.chartal_application.details


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android_academy.chartal_application.App
import com.android_academy.chartal_application.adapters.MovieAdapter
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentMoviesListBinding
import com.android_academy.chartal_application.repository.NetworkModule
import com.android_academy.chartal_application.util.NetworkStatus
import com.android_academy.chartal_application.util.ResProvider


class FragmentMoviesList : Fragment(), MovieAdapter.Listener {

    private val resProvider = ResProvider(App.instance)
    private val networkStatus = NetworkStatus(App.instance)
    private val moviesViewModel: MoviesViewModel by viewModels {
        MoviesListViewModelFactory(
            NetworkModule.filmsRepository,
            resProvider,
            networkStatus
        )
    }
    var flag = true
    private var isUserFilmsTableEmpty: Boolean = true
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
    ): View {
        if (savedInstanceState != null) {
            flag = savedInstanceState.getBoolean(BTN_FLAG)
            isUserFilmsTableEmpty = savedInstanceState.getBoolean(USER_FILM_TABLE_EMPTY)
        }
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
        initProgressBar()
        loadFilms()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                flag = true
                hideInformEmptyUserTable()
                moviesViewModel.getSearchMovies(query)
                return true
            }
        })
        binding.tvMoviesList.setOnClickListener {
            flag = true
            moviesViewModel.getDefaultList()
            hideInformEmptyUserTable()
        }
        binding.ivBtnToBd.setOnClickListener {
            flag = false
            moviesViewModel.getListOfMovieFromUserDatabase()
            if (isUserFilmsTableEmpty) {
                showInformEmptyUserTable()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BTN_FLAG, flag)
        outState.putBoolean(USER_FILM_TABLE_EMPTY, isUserFilmsTableEmpty)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun oItemClicked(movie: Movie, position: Int) {
        listener?.addGalleryFragment(movie, movieAdapter.items, position, flag)
    }

    override fun onListScrolled() {
        moviesViewModel.addNextListOfMovies()
    }

    private fun initErrorHandler() {
        moviesViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    private fun loadFilms() {

        moviesViewModel.moviesMediatorLiveData.observe(viewLifecycleOwner, Observer { it ->
            movieAdapter.addItems(it)
        })
        moviesViewModel.userMovies.observe(viewLifecycleOwner, Observer {
            if (!flag) {
                moviesViewModel.getListOfMovieFromUserDatabase()
            }
        })
        moviesViewModel.isUserFilmsTableEmpty.observe(viewLifecycleOwner, Observer {
            isUserFilmsTableEmpty = when (it) {
                1 -> false
                else -> true
            }
        })

        if(!flag && isUserFilmsTableEmpty) {
            showInformEmptyUserTable()
        }
    }

    private fun initProgressBar() {
        moviesViewModel.isProgressBarVisible.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it
        })
    }

    private fun hideInformEmptyUserTable() {
        binding.ivPersonalVideo.visibility = View.INVISIBLE
        binding.tvPersonalVideo.visibility = View.INVISIBLE
    }

    private fun showInformEmptyUserTable() {
        binding.ivPersonalVideo.visibility = View.VISIBLE
        binding.tvPersonalVideo.visibility = View.VISIBLE
    }

    companion object {
        private const val BTN_FLAG = "FLAG"
        private const val USER_FILM_TABLE_EMPTY = "USER_FILM_TABLE_EMPTY"
    }

}