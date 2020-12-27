package com.android_academy.chartal_application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.ActivityMainBinding
import com.android_academy.chartal_application.details.FragmentMoviesDetails
import com.android_academy.chartal_application.details.TransactionsFragmentClicks


class MainActivity : AppCompatActivity(), TransactionsFragmentClicks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun addFragmentMoviesDetails(movie: Movie) {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, FragmentMoviesDetails.newInstance(movie))
            .addToBackStack(null)
            .commit()
    }

    override fun addFragmentMoviesList() {
        supportFragmentManager.popBackStack()
    }

}