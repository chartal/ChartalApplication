package com.android_academy.chartal_application.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.details.DetailsMovieFragment

class ViewPagerAdapter(
    fragment: Fragment,
    private val movies: List<Movie>,
    private val flag: Boolean
) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = movies.size
    override fun createFragment(position: Int): Fragment {
        return DetailsMovieFragment.newInstance(movies[position], flag)
    }
}