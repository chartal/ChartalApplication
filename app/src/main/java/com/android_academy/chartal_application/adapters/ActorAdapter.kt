package com.android_academy.chartal_application.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Actor
import com.bumptech.glide.Glide


class ActorAdapter(val context: Context) :
    RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    private val items = mutableListOf<Actor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.actor_item, parent, false)
        return ActorViewHolder(view)
}



    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItems(newItems: List<Actor>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

     inner class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster = itemView.findViewById<ImageView>(R.id.iv_actor)
        private val actorFullName = itemView.findViewById<TextView>(R.id.tv_full_actor_name)

        fun bind(actor: Actor) {

            Glide
                .with(context)
                .load(actor.picture)
                .into(poster)
            actorFullName.text = actor.name
        }
    }
}