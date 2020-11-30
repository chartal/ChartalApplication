package com.android_academy.chartal_application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android_academy.chartal_application.databinding.ActivityMainBinding
import com.android_academy.chartal_application.details.FragmentMoviesDetails
import com.android_academy.chartal_application.details.FragmentMoviesList
import com.android_academy.chartal_application.details.TransactionsFragmentClicks

class MainActivity : AppCompatActivity(), TransactionsFragmentClicks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun addFragmentMoviesDetails() {
        val fragment = FragmentMoviesDetails()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun addFragmentMoviesList() {
        onBackPressed()
    }

    override fun onBackPressed() {
        val lastFragment = supportFragmentManager.fragments.last()
        if (lastFragment is FragmentMoviesList) {
            super.onBackPressed()
        }
        if (lastFragment is FragmentMoviesDetails) {
            supportFragmentManager.beginTransaction()
                .remove(lastFragment)
                .commit()
        } else {
            super.onBackPressed()
        }
    }

}