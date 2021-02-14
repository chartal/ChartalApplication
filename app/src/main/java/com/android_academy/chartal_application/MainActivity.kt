package com.android_academy.chartal_application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.ActivityMainBinding
import com.android_academy.chartal_application.details.DetailsMovieFragment
import com.android_academy.chartal_application.details.TransactionsFragmentClicks
import com.android_academy.chartal_application.repository.NetworkModule
import com.android_academy.chartal_application.viewpager.GalleryFragment
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), TransactionsFragmentClicks {

    private lateinit var binding: ActivityMainBinding
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }
    private var scope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.IO +
                exceptionHandler
    )
    private val repository = NetworkModule.filmsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, GalleryFragment.newInstance(movies, position, flag))
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

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toIntOrNull()
                if (id != null) {
                    scope.launch {
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