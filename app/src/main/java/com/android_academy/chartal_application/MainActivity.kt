package com.android_academy.chartal_application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android_academy.chartal_application.databinding.ActivityMainBinding
import com.android_academy.chartal_application.details.FragmentMoviesDetails
import com.android_academy.chartal_application.details.FragmentMoviesList

class MainActivity : AppCompatActivity(), FragmentMoviesList.TransactionsFragmentClicks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun addFragmentMoviesDetails() {
        val fragment = FragmentMoviesDetails()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}