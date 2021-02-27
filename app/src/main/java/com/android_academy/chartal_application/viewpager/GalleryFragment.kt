package com.android_academy.chartal_application.viewpager

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.databinding.FragmentGalleryBinding
import com.google.android.material.transition.MaterialContainerTransform


class GalleryFragment() : Fragment(R.layout.fragment_gallery) {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_container
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(Color.rgb(148,0,211))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        this.arguments?.let {
            val movies = it.getParcelableArrayList<Movie>(GALLERY_MOVIES)!!
            val position = it.getInt(GALLERY_MOVIE_POSITION)
            val flag = it.getBoolean(FLAG)
            binding.viewPager.apply {
                adapter = ViewPagerAdapter(this@GalleryFragment, movies, flag)
                setCurrentItem(position, false)
            }
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val GALLERY_MOVIES = "GALLERY_MOVIES"
        private const val GALLERY_MOVIE_POSITION = "ARGS_MOVIE_POSITION"
        private const val FLAG = "FLAG"

        fun newInstance(movies: List<Movie>, position: Int, flag: Boolean): GalleryFragment {
            return GalleryFragment().apply {
                arguments = bundleOf(
                    GALLERY_MOVIES to movies,
                    GALLERY_MOVIE_POSITION to position,
                    FLAG to flag
                )
            }
        }
    }
}