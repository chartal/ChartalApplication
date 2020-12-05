package com.android_academy.chartal_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Movie


class MovieAdapter(private val clickListener: Listener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    val items = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, null)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItems(newItems: List<Movie>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    interface Listener {
        fun oItemClicked(movie: Movie)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.setOnClickListener {
                clickListener.oItemClicked(movie)
            }

            itemView.findViewById<TextView>(R.id.fr_tv_age).text = movie.age
            itemView.findViewById<ImageView>(R.id.fr_iv_poster).setImageResource(movie.posterRes)
            itemView.findViewById<TextView>(R.id.tv_title).text = movie.title
            itemView.findViewById<TextView>(R.id.fr_tv_movie_description).text = movie.description
            itemView.findViewById<TextView>(R.id.fr_tv_movie_review).text = movie.review
            itemView.findViewById<RatingBar>(R.id.fr_rating_bar).rating = movie.rating
            itemView.findViewById<TextView>(R.id.fr_tv_time).text = movie.time
            if(movie.favorite){
                itemView.findViewById<ImageView>(R.id.iv_baseline_favorite).setImageResource(R.drawable.ic_baseline_favorite_true_24)
            }else{
                itemView.findViewById<ImageView>(R.id.iv_baseline_favorite).setImageResource(R.drawable.ic_baseline_favorite_24)
            }

        }
    }
}