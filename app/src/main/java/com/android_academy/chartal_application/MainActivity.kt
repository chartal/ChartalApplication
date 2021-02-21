package com.android_academy.chartal_application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.ActivityMainBinding
import com.android_academy.chartal_application.databinding.ViewHolderMovieBinding
import com.android_academy.chartal_application.details.DetailsMovieFragment
import com.android_academy.chartal_application.details.TransactionsFragmentClicks
import com.android_academy.chartal_application.repository.NetworkModule
import com.android_academy.chartal_application.viewpager.GalleryFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), TransactionsFragmentClicks {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingItem: ViewHolderMovieBinding
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }
    private val repository = NetworkModule.filmsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingItem = ViewHolderMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            intent?.let(::handleIntent)
        }
    }

    override fun addGalleryFragment(
        movie: Movie,
        movies: List<Movie>,
        position: Int,
        flag: Boolean
    ) {
        val galleryMoviesDetailTransitionName = getString(R.string.fragment_gallery_transition_name)
        val itemMovie = bindingItem.itemMovie
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(android.R.id.content, GalleryFragment.newInstance(movies, position, flag))
            .addSharedElement(itemMovie, galleryMoviesDetailTransitionName)
            .addToBackStack(null)
            .commit()
    }

    override fun addFragmentMoviesList() {
        supportFragmentManager.popBackStack()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toIntOrNull()
                if (id != null) {
                    lifecycleScope.launch(Dispatchers.IO + exceptionHandler) {
                        val movie = repository.findMovieById(id)
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                android.R.id.content,
                                DetailsMovieFragment.newInstance(movie, true)
                            )
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }
    }
}