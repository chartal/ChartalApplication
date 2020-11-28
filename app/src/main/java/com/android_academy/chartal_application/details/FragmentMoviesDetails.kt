package com.android_academy.chartal_application.details

import androidx.fragment.app.Fragment
import com.android_academy.chartal_application.R

class FragmentMoviesDetails : Fragment(R.layout.fragment_movie_details) {

    companion object {
        fun newInstance(): FragmentMoviesDetails {
            return FragmentMoviesDetails()
        }

    }
}
