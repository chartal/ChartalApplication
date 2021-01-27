package com.android_academy.chartal_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Movie


class MovieAdapter(private val clickListener: Listener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val items = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(items[position])
        if (position == (itemCount-1)) {
            clickListener.onListScrolled()
        }
    }

    fun addItems(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    interface Listener {
        fun oItemClicked(movie: Movie)
        fun onListScrolled()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val age = itemView.findViewById<TextView>(R.id.fr_tv_age)
        private val poster = itemView.findViewById<ImageView>(R.id.fr_iv_poster)
        private val title = itemView.findViewById<TextView>(R.id.tv_title)
        private val description = itemView.findViewById<TextView>(R.id.fr_tv_movie_description)
        private val review = itemView.findViewById<TextView>(R.id.fr_tv_movie_review)
        private val ratingBar = itemView.findViewById<RatingBar>(R.id.fr_rating_bar)
        private val time = itemView.findViewById<TextView>(R.id.fr_tv_time)
        private val favorite = itemView.findViewById<ImageView>(R.id.iv_baseline_favorite)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.oItemClicked(items[position])
                }
            }
        }

        fun bind(movie: Movie) {
            poster.load(movie.poster)
            age.text = movie.minimumAge.toString()+"+"
            title.text = movie.title
            description.text = movie.genres?.joinToString()
            review.text = movie.numberOfRatings.toString()
            ratingBar.rating = movie.ratings
            time.text = movie.runtime.toString() + " MIN"
            if (movie.ratings >= 4) {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_true_24)
            } else {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }
    }
}